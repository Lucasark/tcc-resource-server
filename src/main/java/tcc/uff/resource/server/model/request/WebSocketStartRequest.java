package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcc.uff.resource.server.model.enums.CommandWebSocketEnum;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketStartRequest {

    private String courseId;

    private Instant date;

    private CommandWebSocketEnum type;

}
