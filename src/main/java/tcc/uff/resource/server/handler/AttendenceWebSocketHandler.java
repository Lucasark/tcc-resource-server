package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.enums.CommandWebSocketEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.WebSocketStartRequest;
import tcc.uff.resource.server.model.response.ErrorResponse;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.socket.CloseStatus.POLICY_VIOLATION;
import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AttendenceWebSocketHandler extends AbstractWebSocketHandler {

    private final TaskScheduler taskScheduler;

    private final Map<String, AttendanceHandler> attendances;

    private final CourseService courseService;

    private final FrequencyServiceImpl frequencyService;

    private String courseId;

    private Instant date;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        WebSocketStartRequest request = new WebSocketStartRequest();

        try {
            request = objectMapper.readValue(message.getPayload().toString(), WebSocketStartRequest.class);
            courseId = request.getCourseId();
            date = request.getDate();
        } catch (Exception e) {
            log.error("GENERICO!", e);
        }

        if (CommandWebSocketEnum.START.equals(request.getType())) {
            var principal = session.getPrincipal();

            if (principal == null || principal.getName() == null) {
                session.close(SERVER_ERROR.withReason("Voce deve estar autenticado"));
                return;
            }


            if (!courseService.isOwnerByUser(principal.getName(), courseId)) {
                session.close(SERVER_ERROR.withReason("Voce não é o dono do Curso!"));
                return;
            }

            if (attendances.containsKey(courseId)) {

                var oldAttendence = attendances.get(courseId);

                if (oldAttendence.getSession().isOpen()) {
                    oldAttendence.getSession().close();
                } else {
                    finisheSessionByCourseId(courseId);
                }

            }

            var actived = frequencyService.allActivedFrequencyByCourse(courseId);

            if (!actived.isEmpty()) {
                String result = actived.stream()
                        .map(v -> v.getDate().toString())
                        .collect(Collectors.joining(", "));

                var response = ErrorResponse.builder()
                        .message("Já existe uma Frequencia Ativa")
                        .description(result)
                        .build();

                session.close(POLICY_VIOLATION.withReason(new ObjectMapper().writeValueAsString(response)));
            }

//                Fluxo para criar uma nova
            frequencyService.initFrenquency(courseId, date);

            var attendence = AttendanceHandler.builder()
                    .courseId(courseId)
                    .session(session)
                    .date(date)
                    .build();

            attendances.put(courseId, attendence);

            var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendence), Duration.ofSeconds(5));

            attendances.get(courseId).setScheduled(schedule);
        }
        if (CommandWebSocketEnum.STOP.equals(request.getType())) {
            finisheSessionByCourseId(courseId);
            session.close();
            //TODO: Colocar faltar
        }
    }

    private void finisheSessionByCourseId(String courseId) {
        if (attendances.containsKey(courseId)) {
            attendances.get(courseId).getScheduled().cancel(false);
            attendances.remove(courseId);
        }
    }

}
