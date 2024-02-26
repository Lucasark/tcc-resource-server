package tcc.uff.resource.server.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcc.uff.resource.server.model.enums.CommandRequestEnum;

import java.time.Instant;

import static tcc.uff.resource.server.utils.Constants.OFF_DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyHandlerResponse {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFF_DATE_TIME_PATTERN)
    private Instant date;

    private String code;

    private String course;

    private CommandRequestEnum status;

}
