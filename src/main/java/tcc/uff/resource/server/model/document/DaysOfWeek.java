package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DaysOfWeek {

    private LocalDateTime start;

    private LocalDateTime end;

}
