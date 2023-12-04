package tcc.uff.resource.server.model.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcc.uff.resource.server.converter.Instant2StringConverter;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendenceRequest {

    @JsonSerialize(converter = Instant2StringConverter.class)
    private Instant date;

}
