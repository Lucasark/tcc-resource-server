package tcc.uff.resource.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.model.enums.AttendanceOriginEnum;
import tcc.uff.resource.server.model.enums.CommandRequestEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.AttendanceRequest;
import tcc.uff.resource.server.model.response.FrequencyHandlerResponse;
import tcc.uff.resource.server.repository.CourseRepository;
import tcc.uff.resource.server.utils.GenerateString;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrequencyHandlerServiceImpl {

    private final AttendanceServiceImpl attendanceService;

    private final CourseRepository courseRepository;

    private final FrequencyServiceImpl frequencyService;

    public FrequencyHandlerResponse handlerFrenquency(String courseId, AttendanceRequest request) throws RuntimeException {

        var courseDocument = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("N achou Classe!"));

        if (CommandRequestEnum.START.equals(request.getType())) {

            var code = GenerateString.generateRandomString(10);

            var attendance = AttendanceHandler.builder()
                    .courseId(courseId)
                    .date(request.getDate())
                    .code(code)
                    .origin(AttendanceOriginEnum.STATIC)
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .build();

//            var frequencyOptional = frequencyService.getFrequencyByCourseAndDate(courseId, date);
//
//            if (frequencyOptional.isPresent()) {
//                if (Boolean.TRUE.equals(frequencyOptional.get().getFinished())) {
//                    var response = ErrorResponse.builder()
//                            .message("Existe uma Frequencia Finalizada para este Curso nesta Data!")
//                            .description(frequencyOptional.get().getDate().toString())
//                            .code("47")
//                            .build();
//
//                    session.close(POLICY_VIOLATION.withReason(new ObjectMapper().writeValueAsString(response)));
//                    return;
//                } else {
//
//                    var response = WebSocketResponse.builder()
//                            .type(CommandResponseWebSocketEnum.WARN)
//                            .value("Existe uma Frequencia criada para este Curso nesta Data!")
//                            .description("A frequencia será retomada para está data deste curso")
//                            .build();
//
//                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
//
//                    finisheSessionByCourseId(courseId);
//                }
//            } else {
//                frequencyService.endLastFrequencyOfCourse(courseId);
//                finisheSessionByCourseId(courseId);
//                frequencyService.initFrenquency(courseId, date);
//            }

            attendanceService.addAttendance(attendance);

            frequencyService.initFrenquency(courseDocument, request.getDate());

        }
        if (CommandRequestEnum.STOP.equals(request.getType())) {
            frequencyService.endLastFrequencyOfCourse(courseId);
        }

        return null;
    }

}
