package tcc.uff.resource.server.model.response.relation.teachercourse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DaysOfWeek {

    private Integer startHour;

    private Integer startMinute;

    private Integer endHour;

    private Integer endMinute;

    private Integer dayOfWeek;

}
