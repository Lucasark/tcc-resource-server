package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tcc.uff.resource.server.model.document.AttendenceHandler;
import tcc.uff.resource.server.utils.GenerateString;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendenceHandlerComponent {

    private final SimpMessagingTemplate template;

    @Autowired
    private Map<String, AttendenceHandler> attendences = new HashMap<>();

    @Scheduled(fixedRate = 3000)
    public void handler() {

        //TODO: TIRAR DEPOIS DE X TEMPOS

        attendences.forEach((course, attendence) -> {
            var codeNew = GenerateString.generateRandomString(10);

            attendence.setRepeat(attendence.getRepeat() + 1);
            attendence.setCode(codeNew);

            attendences.replace(course, attendence);

            template.convertAndSendToUser(course, "/topic/" + attendence.getId(), codeNew);
        });
    }
}
