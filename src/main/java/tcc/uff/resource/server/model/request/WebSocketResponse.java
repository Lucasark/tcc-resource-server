package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tcc.uff.resource.server.model.enums.CommandResponseWebSocketEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketResponse {

    @NonNull
    private String value;

    @NonNull
    private CommandResponseWebSocketEnum type;

    private String description;

}
