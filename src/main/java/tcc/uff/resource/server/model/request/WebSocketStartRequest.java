package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketStartRequest {

    private String courseId;

    //TODO: Mudar para um formato!
    private String date;

    private String type;

}
