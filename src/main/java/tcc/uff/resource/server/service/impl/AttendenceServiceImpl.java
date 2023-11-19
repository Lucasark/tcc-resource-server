package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.document.Attendance;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.model.response.entity.AttendenceResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.AttendenceService;
import tcc.uff.resource.server.utils.GenerateString;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AttendenceServiceImpl implements AttendenceService {

    @Autowired
    private Map<String, AttendenceHandler> attendences = new HashMap<>();

    private final CourseRepository courseRepository;
    private final FrequencyRepository frequencyRepository;
    private final UserRepository userRepository;

    public AttendenceResponse createAttendence(String course, OffsetDateTime date) {

        var code = GenerateString.generateRandomString(10);

        var courseDocument = courseRepository.findById(course).orElseThrow(() -> new RuntimeException("N achou Classe!"));

        var frequencyNew = FrequencyDocument.builder()
                .course(courseDocument)
                .date(date.toInstant())
                .build();

        frequencyRepository.save(frequencyNew);

        var handler = AttendenceHandler.builder()
                .id(new ObjectId().toString())
                .date(date.toInstant())
                .code(code)
                .build();

        attendences.put(courseDocument.getId(), handler);

        return AttendenceResponse.builder()
                .id(handler.getId())
                .date(date)
                .code(handler.getCode())
                .course(courseDocument.getId())
                .build();
    }

    public void updateFrequency(String course, String code, String member) {
        var attendenceHandler = attendences.get(course);

        if (attendenceHandler.getCode().equals(code)) {
            var frequency = frequencyRepository.findByDate(attendenceHandler.getDate())
                    .orElseThrow(() -> new RuntimeException("Frequencia n existe!"));

            if (frequency.getAttendances().stream().anyMatch(a -> a.getStudent().getEmail().equals(member)))
                throw new RuntimeException("Já marcou presença!");

            var user = userRepository.findById(member).orElseThrow(() -> new RuntimeException("N achou User!"));

            frequency.getAttendances().add(Attendance.builder()
                    .status(Boolean.TRUE)
                    .student(user)
                    .build());

            frequencyRepository.save(frequency);
        }
    }
}
