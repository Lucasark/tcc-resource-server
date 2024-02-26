package tcc.uff.resource.server.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tcc.uff.resource.server.model.enums.AttendanceStatusEnum;
import tcc.uff.resource.server.model.enums.AttendanceOriginEnum;

@Getter
@Setter
@Builder
public class AttendanceActivedResponse {

    private AttendanceStatusEnum status;

    private AttendanceOriginEnum origin;

    private String latitude;

    private String longitude;

}
