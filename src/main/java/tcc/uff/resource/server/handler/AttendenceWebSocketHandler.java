package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
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

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        log.info("handleMessage: " + message.getPayload());
        super.handleMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("handleTransportError: " + exception);
        super.handleTransportError(session, exception);
    }

    private final Map<String, AttendenceHandler> attendences;

    private final CourseService courseService;

    private final FrequencyServiceImpl frequencyService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        var principal = session.getPrincipal();

        if (principal == null || principal.getName() == null) {
            session.close(SERVER_ERROR.withReason("Voce deve estar autenticado"));
            return;
        }

        String courseId = (String) session.getAttributes().get("courseId");
        Instant date = (Instant) session.getAttributes().get("date");

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

        //Fluxo para criar uma nova
        frequencyService.initFrenquency(courseId, date);

        var attendence = AttendenceHandler.builder()
                .courseId(courseId)
                .session(session)
                .date(date)
                .build();

        attendences.put(courseId, attendence);

        var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendence), Duration.ofSeconds(5));

        attendences.get(courseId).setScheduled(schedule);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var principal = session.getPrincipal();
        Assert.notNull(principal, "Principal Null");

        String courseId = (String) session.getAttributes().get("courseId");

        finisheSessionByCourseId(courseId);
    }

    private void finisheSessionByCourseId(String courseId) {
        if (attendences.containsKey(courseId)) {
            attendences.get(courseId).getScheduled().cancel(false);
            attendences.remove(courseId);
        }
    }

}
