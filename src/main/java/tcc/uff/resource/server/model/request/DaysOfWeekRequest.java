package tcc.uff.resource.server.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DaysOfWeekRequest {

    private LocalDateTime start;

    private LocalDateTime end;

}
