package tcc.uff.resource.server.model.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import tcc.uff.resource.server.model.enums.AttendanceOriginEnum;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;

@Getter
@Setter
@Builder
public class AttendanceHandler {

    @NonNull
    private String courseId;

    private String code;

    @NonNull
    private Instant date;

    @Builder.Default
    private Integer repeat = 0;

    private WebSocketSession session;

    private ScheduledFuture<?> scheduled;

    private AttendanceOriginEnum origin;

    private String latitude;

    private String longitude;
}
