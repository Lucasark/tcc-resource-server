package tcc.uff.resource.server.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationResponse {

    private String latitude;

    private String longitude;

}
