package tcc.uff.resource.server.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class GenericOption implements Serializable {

    private Integer id;

    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

}
