package tcc.uff.resource.server.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.Assert;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.handler.AttendenceHandler;
import tcc.uff.resource.server.service.CourseService;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

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

//    private final FrequencyServiceImpl frequencyService;

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

        /*TODO:

        - Impede de criar uma nova frequencia se uma ainda está ativa?

            Validar com o Joao

        - Recupera a anterior? Esse aqui n da pq a session é unitaria, so se fosse um STOPM, mas é canhao pra matar formiga, alem de violar seguranca

            Viola o fluxo: CONNECT -> HANDSHAKE -> MESSAGE, pq iria fakear o CONNECT com antigo

        - Finaliza a anterior e inicia uma nova?

            Atualmente essa, é o que mercado de Socket implementa, WPP, Telegram e afins; claro, esses que não STOMP, como MessageServer, já que é Biderecioanl
        */

        if (attendences.containsKey(courseId)) {

            //TODO: Se já em uma sessao aberta? Atualmente vibe WPP e Telegram
            var oldAttendence = attendences.get(courseId);

            if (oldAttendence.getSession().isOpen()) {
                oldAttendence.getSession().close();
            } else {
                //Muito especifico... mas vai que, neh..
                finisheSessionByCourseId(courseId);
            }

        }

//        isCourseHasActivedFrequency(courseDocument);

//        frequencyService.initFrenquency();


        //Fluxo para criar uma nova

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
