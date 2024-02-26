package tcc.uff.resource.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import tcc.uff.resource.server.model.document.Attendance;
import tcc.uff.resource.server.model.document.UserAlias;
import tcc.uff.resource.server.model.enums.AttendanceEnum;
import tcc.uff.resource.server.model.enums.AttendanceStatusEnum;
import tcc.uff.resource.server.model.enums.CommandResponseWebSocketEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.WebSocketResponse;
import tcc.uff.resource.server.model.response.AttendanceActivedResponse;
import tcc.uff.resource.server.repository.FrequencyRepository;
import tcc.uff.resource.server.repository.UserRepository;
import tcc.uff.resource.server.service.AttendanceService;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final Map<String, AttendanceHandler> attendances;
    private final FrequencyRepository frequencyRepository;
    private final FrequencyServiceImpl frequencyService;
    private final UserRepository userRepository;

    public void updateFrequency(String course, String code, String member) {
        var attendanceHandler = attendances.get(course);

        if (Objects.nonNull(attendanceHandler) && attendanceHandler.getCode().equals(code)) {
            var frequency = frequencyRepository.findByDateAndCourseId(attendanceHandler.getDate(), course)
                    .orElseThrow(() -> new RuntimeException("Frequencia n existe!"));

            var user = userRepository.findById(member)
                    .orElseThrow(() -> new RuntimeException("N achou User!"));

            var attendence = frequency.getAttendances().stream()
                    .filter(a -> a.getStudent().getEmail().equals(member))
                    .findAny()
                    .orElseGet(() -> Attendance.builder()
                            .status(AttendanceEnum.PRESENT)
                            .student(user)
                            .build());

            if (attendence.getStatus().equals(AttendanceEnum.PRESENT)){
                throw new RuntimeException("Aluno já marcou presença!");
            }

            attendence.setStatus(AttendanceEnum.PRESENT);
            frequency.getAttendances().add(attendence);
            frequencyRepository.save(frequency);

            var alias = user.getAliases().stream()
                    .filter(a -> a.getCourseId().equals(course))
                    .findAny()
                    .orElseGet(() -> UserAlias.builder().name("S/A").build());

            try {
                var response = WebSocketResponse.builder()
                        .type(CommandResponseWebSocketEnum.MEMBER_INCLUDED)
                        .description("Aluno marcou presença")
                        .value(alias.getName())
                        .build();

                if (attendanceHandler.getSession().isOpen())
                    attendanceHandler.getSession().sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));

            } catch (IOException e) {
                log.error("ERROR: ", e);
            }

        } else {
            throw new RuntimeException("N foi possivel marcar presença");
        }
    }

    public AttendanceActivedResponse isActived(String courseId) {

        var attendance = attendances.get(courseId);

        if (Objects.nonNull(attendance)) {

            return AttendanceActivedResponse.builder()
                    .origin(attendance.getOrigin())
                    .status(AttendanceStatusEnum.STARTED)
                    .latitude(attendance.getLatitude())
                    .longitude(attendance.getLongitude())
                    .build();
        }

        return AttendanceActivedResponse.builder().status(AttendanceStatusEnum.NOT_STARTED).build();
    }

    public void updateAttedentceStatusByMember(String frequencyId, String memberId, Integer status) {
        var toAttendance = AttendanceEnum.fromId(status);

        var frequency = frequencyRepository.findById(frequencyId)
                .orElseThrow(() -> new RuntimeException("N existe Frequencia"));

        for (Attendance attendance : frequency.getAttendances()) {
            if (attendance.getStudent().getEmail().equals(memberId)) {
                attendance.setStatus(toAttendance);
                frequencyRepository.save(frequency);
                return;
            }
        }

        frequencyRepository.addInSet(frequencyId, "attendances", Attendance.builder()
                .student(userRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Membro n existe")))
                .status(toAttendance)
                .build()
        );
    }

    @Override
    public void removeAttendanceByCourseId(String courseId) {
        var attendence = attendances.get(courseId);
        frequencyService.endLastFrequencyOfCourse(courseId);
        attendence.getScheduled().cancel(true);
        attendances.remove(courseId);
    }

    public void addAttendance(AttendanceHandler attendanceHandler) {
        attendances.put(attendanceHandler.getCourseId(), attendanceHandler);
    }
}
