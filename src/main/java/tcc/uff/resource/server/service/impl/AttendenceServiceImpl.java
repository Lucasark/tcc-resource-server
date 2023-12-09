package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.Attendance;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.enums.AttendenceEnum;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.AttendenceService;
import tcc.uff.resource.server.service.mongooperations.MongoOperationsService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendenceServiceImpl implements AttendenceService {

    private final Map<String, AttendenceHandler> attendences;
    private final FrequencyRepository frequencyRepository;
    private final UserRepository userRepository;
    private final MongoOperationsService mongoOperationsService;

    public void updateFrequency(String course, String code, String member) {
        var attendenceHandler = attendences.get(course);

        if (attendenceHandler.getCode().equals(code)) {
            var frequency = frequencyRepository.findByDateAndCourseId(attendenceHandler.getDate(), course)
                    .orElseThrow(() -> new RuntimeException("Frequencia n existe!"));

            if (frequency.getAttendances().stream().anyMatch(a -> a.getStudent().getEmail().equals(member)))
                throw new RuntimeException("Já marcou presença!");

            var user = userRepository.findById(member)
                    .orElseThrow(() -> new RuntimeException("N achou User!"));

            var a = Attendance.builder()
                    .status(AttendenceEnum.PRESENT)
                    .student(user)
                    .build();

            mongoOperationsService.addInSet("id", frequency.getId(), "attendances", a, FrequencyDocument.class);
        }
    }

    public void updateAttedentceStatusByMember(String frequencyId, String memberId, Integer status) {
        var toAttendance = AttendenceEnum.fromId(status);

        var frequency = frequencyRepository.findById(frequencyId)
                .orElseThrow(() -> new RuntimeException("N existe Frequencia"));

        for (Attendance attendance : frequency.getAttendances()) {
            if (attendance.getStudent().getEmail().equals(memberId)) {
                attendance.setStatus(toAttendance);
                frequencyRepository.save(frequency);
                return;
            }
        }

        mongoOperationsService.addInSet("id", frequencyId,
                "attendances", Attendance.builder()
                        .student(userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Membro n existe")))
                        .status(toAttendance)
                        .build(),
                FrequencyDocument.class
        );
    }
}
