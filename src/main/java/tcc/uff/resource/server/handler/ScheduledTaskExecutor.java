package tcc.uff.resource.server.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import tcc.uff.resource.server.model.enums.CommandResponseWebSocketEnum;
import tcc.uff.resource.server.model.handler.AttendanceHandler;
import tcc.uff.resource.server.model.request.WebSocketResponse;
import tcc.uff.resource.server.model.response.ErrorResponse;
import tcc.uff.resource.server.service.AttendanceService;
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

    private static final Integer MAX_REPEAT = 120;

    private final AttendanceHandler attendance;

    private final AttendanceService attendanceService;


    @Override
    public void run() {
        if (!attendance.getRepeat().equals(MAX_REPEAT)) {
            try {
                var code = GenerateString.generateRandomString(10);
                attendance.setCode(code);
                if (attendance.getSession().isOpen()) {
                    log.info("ENVIADO MSG: " + attendance.getCourseId() + " | " + attendance.getDate());

                    var response = WebSocketResponse.builder()
                            .type(CommandResponseWebSocketEnum.CODE)
                            .description("Código para ativação")
                            .value(code)
                            .build();

                    attendance.getSession().sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(response)));
                } else {
                    log.warn("NÃO ENVIOU MSG! MAS AINDA ESTÁ ATIVO: " + attendance.getCourseId() + " | " + attendance.getDate());
                }
                attendance.setRepeat(attendance.getRepeat() + 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            var response = ErrorResponse.builder()
                    .code(String.valueOf(CloseStatus.NORMAL.getCode()))
                    .message("Limite de envios de messagens atingido!")
                    .description("O limite de menssagens foi atingido pelo serviço, portanto a frequencia foi finalizada!")
                    .build();

            try {
                attendance.getSession().close(CloseStatus.NORMAL.withReason(new ObjectMapper().writeValueAsString(response)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            attendanceService.removeAttendanceByCourseId(attendance.getCourseId());
        }
    }
}