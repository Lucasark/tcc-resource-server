package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import tcc.uff.resource.server.model.document.FrequencyDocument;
import tcc.uff.resource.server.model.enums.CommandWebSocketEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.WebSocketStartRequest;
import tcc.uff.resource.server.model.response.ErrorResponse;
import tcc.uff.resource.server.service.AttendanceService;
import tcc.uff.resource.server.service.CourseService;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.socket.CloseStatus.BAD_DATA;
import static org.springframework.web.socket.CloseStatus.POLICY_VIOLATION;
import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

@Slf4j
@RequiredArgsConstructor
public class AttendanceWebSocketHandler extends AbstractWebSocketHandler {

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

        WebSocketStartRequest request = new WebSocketStartRequest();

        try {
            request = objectMapper.readValue(message.getPayload().toString(), WebSocketStartRequest.class);
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

                var oldAttendance = attendances.get(courseId);

                if (oldAttendance.getSession().isOpen()) {
                    oldAttendance.getSession().close();
                } else {
                    finisheSessionByCourseId(courseId);
                }

            }

            var finished = frequencyService.allFinishedFrequencyByCourse(courseId);

            if (finished.stream().anyMatch(auxFrequency -> auxFrequency.getDate().equals(date))) {

                String result = finished.stream()
                        .map(v -> v.getDate().toString())
                        .collect(Collectors.joining(", "));

                var response = ErrorResponse.builder()
                        .message("Já existe uma Frequencia Ativa para este Curso")
                        .description(result)
                        .code("47")
                        .build();

                session.close(POLICY_VIOLATION.withReason(new ObjectMapper().writeValueAsString(response)));
                return;
            }

            //TODO: Talvez de ruim... será que é possivel ter mais de uma frequencia ativa?
            //Caso sim, finaliza todas, ou apenas a ultima?

            var frequency = frequencyService.getLastStartedFrequencyByCourse(courseId);
            frequencyId = frequency.getId();

            if (Optional.ofNullable(frequencyId).isPresent()) {
                if (!frequency.getDate().equals(date)) {
                    finisheFrequency(frequency);
                    startFrequencyByCourseId();
                }
            } else {
                startFrequencyByCourseId();
            }

            var attendance = AttendanceHandler.builder()
                    .frequency(frequencyId)
                    .courseId(courseId)
                    .session(session)
                    .date(date)
                    .build();

            attendances.put(courseId, attendance);

            var schedule = taskScheduler.scheduleAtFixedRate(new ScheduledTaskExecutor(attendance, attendanceService), Duration.ofSeconds(5));

            attendances.get(courseId).setScheduled(schedule);
        }
        if (CommandWebSocketEnum.STOP.equals(request.getType())) {
            finisheSessionByCourseId(courseId);
            session.close();
            finisheFrequency(frequencyService.getLastStartedFrequencyByCourse(courseId));
        }
    }

    private void finisheSessionByCourseId(String courseId) {
        if (attendances.containsKey(courseId)) {
            attendances.get(courseId).getScheduled().cancel(false);
            attendances.remove(courseId);
        }
    }

    private void startFrequencyByCourseId() {
        var startedFrequency = frequencyService.initFrenquency(courseId, date);
        frequencyId = startedFrequency.getId();
    }

    private void finisheFrequency(FrequencyDocument frequencyDocument) {
        frequencyService.endFrenquecy(frequencyDocument);
    }
}
