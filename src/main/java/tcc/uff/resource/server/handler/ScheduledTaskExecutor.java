package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.utils.GenerateString;

import java.io.IOException;

/**
 * Futuro:
 * <p>
 * Utilizar Ping-Pong
 */
@Slf4j
@RequiredArgsConstructor
public class ScheduledTaskExecutor implements Runnable {

    private final AttendanceHandler attendance;

    @Override
    public void run() {
        try {
            var code = GenerateString.generateRandomString(10);
            attendance.setCode(code);
            if (attendance.getSession().isOpen()) {
                attendance.getSession().sendMessage(new TextMessage(code));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}