package tcc.uff.resource.server.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class SelectOption implements Serializable {

    private String option;

}
