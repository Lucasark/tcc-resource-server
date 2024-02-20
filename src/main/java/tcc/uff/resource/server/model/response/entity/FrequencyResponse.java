package tcc.uff.resource.server.model.response.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyResponse {

    //BUG: https://github.com/spring-projects/spring-data-mongodb/issues/4645
    @Field("id")
    private String id;

    private Instant date;

    private Integer status;
}
