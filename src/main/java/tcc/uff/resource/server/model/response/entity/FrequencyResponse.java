package tcc.uff.resource.server.model.response.entity;

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
public class FrequencyResponse {

    private String id;

    @JsonSerialize(converter = Instant2StringConverter.class)
    private Instant date;

    private String course;

}
