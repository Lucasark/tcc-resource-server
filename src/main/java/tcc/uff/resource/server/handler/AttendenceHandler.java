package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tcc.uff.resource.server.repository.AttendenceRepository;
import tcc.uff.resource.server.utils.GenerateString;

@Slf4j
@Component
@RequiredArgsConstructor
public class AttendenceHandler {

    private final SimpMessagingTemplate template;

    private final AttendenceRepository repository;

    @Scheduled(fixedRate = 1000)
    public void handler() {
        repository.findAll().parallelStream().forEach(
                attendence -> {
                    var codeNew = GenerateString.generateRandomString(10);

                    attendence.setRepeat(attendence.getRepeat() + 1);
                    attendence.setCode(codeNew);

                    repository.save(attendence);

                    template.convertAndSendToUser(attendence.getId(), "/topic/" + attendence.getCourse().getId(), codeNew);
                }
        );
    }
}
