package tcc.uff.resource.server.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.handler.ScheduledTaskExecutor;
import tcc.uff.resource.server.model.handler.AttendenceHandler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@RequiredArgsConstructor
public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    private final TaskScheduler taskScheduler;

    private final Map<String, AttendenceHandler> attendences;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        var principal = session.getPrincipal();

        if (principal == null || principal.getName() == null) {
            session.close(SERVER_ERROR.withReason("User must be authenticated"));
            return;
        }

        attendences.put(principal.getName(), AttendenceHandler.builder()
                .id("id")
                .code("code")
                .session(session)
                .date(Instant.now())
                .build());

        var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendences), Duration.ofSeconds(5));

        attendences.get(principal.getName()).setScheduled(schedule);

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var principal = session.getPrincipal();
        Assert.notNull(principal, "Principal Null");

        attendences.get(principal.getName()).getScheduled().cancel(false);
        attendences.remove(principal.getName());
    }

}
