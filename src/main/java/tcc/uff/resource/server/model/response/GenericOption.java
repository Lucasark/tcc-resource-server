package tcc.uff.resource.server.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericOption implements Serializable {

    private Integer id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

}
