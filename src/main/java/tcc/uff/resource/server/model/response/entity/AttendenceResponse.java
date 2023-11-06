package tcc.uff.resource.server.model.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class AttendenceResponse {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = OFF_DATE_TIME_PATTERN)
    private OffsetDateTime date;

    private String code;

    private String course;

}
