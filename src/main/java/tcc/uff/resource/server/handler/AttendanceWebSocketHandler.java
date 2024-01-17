package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.enums.CommandRequestWebSocketEnum;
import tcc.uff.resource.server.model.enums.CommandResponseWebSocketEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.WebSocketRequest;
import tcc.uff.resource.server.model.request.WebSocketResponse;
import tcc.uff.resource.server.model.response.ErrorResponse;
import tcc.uff.resource.server.service.AttendanceService;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.springframework.web.socket.CloseStatus.BAD_DATA;
import static org.springframework.web.socket.CloseStatus.POLICY_VIOLATION;
import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AttendanceWebSocketHandler extends AbstractWebSocketHandler {

    private static final int DURATION_TIME_SECONDS = 30;

    private final TaskScheduler taskScheduler;

    private final Map<String, AttendanceHandler> attendances;

    private final CourseService courseService;

    private final FrequencyServiceImpl frequencyService;

    private final AttendanceService attendanceService;

    private String courseId;

    private Instant date;

    private String frequencyId;


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        WebSocketRequest request;

        try {
            request = objectMapper.readValue(message.getPayload().toString(), WebSocketRequest.class);
            courseId = request.getCourseId();
            date = request.getDate();
        } catch (Exception e) {
            log.error("GENERICO!", e);

            var response = ErrorResponse.builder()
                    .message("Falha no Payload")
                    .description(e.getMessage())
                    .code(String.valueOf(BAD_DATA.getCode()))
                    .build();

            session.close(BAD_DATA.withReason(new ObjectMapper().writeValueAsString(response)));

            return;
        }

        if (CommandRequestWebSocketEnum.START.equals(request.getType())) {
            var principal = session.getPrincipal();

            if (principal == null || principal.getName() == null) {
                session.close(SERVER_ERROR.withReason("Voce deve estar autenticado"));
                return;
            }


            if (!courseService.isOwnerByUser(principal.getName(), courseId)) {
                session.close(SERVER_ERROR.withReason("Voce não é o dono do Curso!"));
                return;
            }

            var attendance = AttendanceHandler.builder()
                    .courseId(courseId)
                    .session(session)
                    .date(date)
                    .build();

            var frequencyOptional = frequencyService.getFrequencyByCourseAndDate(courseId, date);

            if (frequencyOptional.isPresent()) {
                if (Boolean.TRUE.equals(frequencyOptional.get().getFinished())) {
                    var response = ErrorResponse.builder()
                            .message("Existe uma Frequencia Finalizada para este Curso nesta Data!")
                            .description(frequencyOptional.get().getDate().toString())
                            .code("47")
                            .build();

                    session.close(POLICY_VIOLATION.withReason(new ObjectMapper().writeValueAsString(response)));
                    return;
                } else {

                    var response = WebSocketResponse.builder()
                            .type(CommandResponseWebSocketEnum.WARN)
                            .value("Existe uma Frequencia criada para este Curso nesta Data!")
                            .description("A frequencia será retomada para está data deste curso")
                            .build();

                    session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));

                    finisheSessionByCourseId(courseId);
                }
            } else {
                frequencyService.endLastFrequencyOfCourse(courseId);
                startFrequencyByCourseId();
            }

            attendances.put(courseId, attendance);

            var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendance, attendanceService), Duration.ofSeconds(DURATION_TIME_SECONDS));

            attendances.get(courseId).setScheduled(schedule);
        }
        if (CommandRequestWebSocketEnum.STOP.equals(request.getType())) {
            finisheSessionByCourseId(courseId);
            session.close();
            frequencyService.getLastStartedFrequencyByCourse(courseId).ifPresent(this::finisheFrequency);
        }
    }

    private void finisheSessionByCourseId(String courseId) {
        if (attendances.containsKey(courseId)) {
            attendances.get(courseId).getScheduled().cancel(true);
            attendances.remove(courseId);
        }
    }

    private void startFrequencyByCourseId() {
        var startedFrequency = frequencyService.initFrenquency(courseId, date);
        frequencyId = startedFrequency.getId();
    }

    private void finisheFrequency(FrequencyDocument frequencyDocument) {
        frequencyService.endFrenquecyByCourse(frequencyDocument);
    }
}
