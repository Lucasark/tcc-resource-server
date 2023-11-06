package tcc.uff.resource.server.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static tcc.uff.resource.server.utils.Constants.OFF_DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendenceRequest {

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFF_DATE_TIME_PATTERN)
    private OffsetDateTime date;

}
