package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.utils.GenerateString;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendenceHandlerComponent {

//    private final SimpMessagingTemplate template;

    private final Map<String, AttendenceHandler> attendences;

    private final Map<String, WebSocketSession> sessions;

    @Scheduled(fixedRate = 3000)
    public void handler() {

        //TODO: TIRAR DEPOIS DE X TEMPOS


        sessions.forEach((u, s) -> {
            try {
                s.sendMessage(new TextMessage(GenerateString.generateRandomString(10)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

//        attendences.forEach((course, attendence) -> {
//            var codeNew = GenerateString.generateRandomString(10);
//
//            attendence.setRepeat(attendence.getRepeat() + 1);
//            attendence.setCode(codeNew);
//
//            attendences.replace(course, attendence);
//
////            template.convertAndSendToUser(course, "/topic/" + attendence.getId(), codeNew);
//        });
    }
}
