package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcc.uff.resource.server.model.enums.CommandRequestWebSocketEnum;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketRequest {

    private String courseId;

    private Instant date;

    private CommandRequestWebSocketEnum type;

}