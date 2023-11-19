package tcc.uff.resource.server.model.response.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DaysOfWeekResponse {

    private LocalDateTime start;

    private LocalDateTime end;

}
