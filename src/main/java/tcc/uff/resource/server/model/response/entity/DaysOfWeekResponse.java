package tcc.uff.resource.server.model.response.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tcc.uff.resource.server.converter.Instant2StringConverter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaysOfWeekResponse implements Serializable {

    @JsonSerialize(converter = Instant2StringConverter.class)
    private Instant start;

    @JsonSerialize(converter = Instant2StringConverter.class)
    private Instant end;

}
