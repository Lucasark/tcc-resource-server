package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.model.request.WebSocketStartRequest;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AttendenceWebSocketHandler extends AbstractWebSocketHandler {

    private final TaskScheduler taskScheduler;

    private final Map<String, AttendenceHandler> attendences;

    private final CourseService courseService;

    private final FrequencyServiceImpl frequencyService;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            var request = objectMapper.readValue(message.getPayload().toString(), WebSocketStartRequest.class);

            String courseId = request.getCourseId();

            Instant date = Instant.parse(request.getDate());

            if (request.getType().equals("START")) {
                var principal = session.getPrincipal();

                if (principal == null || principal.getName() == null) {
                    session.close(SERVER_ERROR.withReason("Voce deve estar autenticado"));
                    return;
                }


                if (!courseService.isOwnerByUser(principal.getName(), courseId)) {
                    session.close(SERVER_ERROR.withReason("Voce não é o dono do Curso!"));
                    return;
                }

                if (attendences.containsKey(courseId)) {

                    var oldAttendence = attendences.get(courseId);

                    if (oldAttendence.getSession().isOpen()) {
                        oldAttendence.getSession().close();
                    } else {
                        finisheSessionByCourseId(courseId);
                    }

                }

//        var actived = frequencyService.allActivedFrequencyByCourse(courseId);
//
//        if (!actived.isEmpty()) {
//            String result = actived.stream()
//                    .map(v -> v.getDate().toString())
//                    .collect(Collectors.joining(", "));
//
//            var response = ErrorResponse.builder()
//                    .message("Já existe uma Frequencia Ativa")
//                    .description(result)
//                    .build();
//
//            session.close(POLICY_VIOLATION.withReason(new ObjectMapper().writeValueAsString(response)));
//        }

                //Fluxo para criar uma nova
//        frequencyService.initFrenquency(courseId, date);

                var attendence = AttendenceHandler.builder()
                        .courseId(courseId)
                        .session(session)
                        .date(date)
                        .build();

                attendences.put(courseId, attendence);

                var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendence), Duration.ofSeconds(5));

                attendences.get(courseId).setScheduled(schedule);
            }
            if (request.getType().equals("STOP")) {
                finisheSessionByCourseId(courseId);
                session.close();
            }
        } catch (Exception e) {
            log.error("GENERICO!", e);
        }

    }

    private void finisheSessionByCourseId(String courseId) {
        if (attendences.containsKey(courseId)) {
            attendences.get(courseId).getScheduled().cancel(false);
            attendences.remove(courseId);
        }
    }

}
