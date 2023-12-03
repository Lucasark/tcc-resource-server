package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.utils.GenerateString;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendenceHandlerComponent {

    private final Map<String, AttendenceHandler> attendences;

    @Scheduled(fixedRate = 3000)
    public void handler() {

        attendences.forEach((u, a) -> {
            try {
                a.getSession().sendMessage(new TextMessage(GenerateString.generateRandomString(10)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
