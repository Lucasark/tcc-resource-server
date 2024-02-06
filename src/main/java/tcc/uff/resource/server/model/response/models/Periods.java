package tcc.uff.resource.server.model.response.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tcc.uff.resource.server.model.response.SelectOption;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class Periods implements Serializable {

    private List<SelectOption> periods;

}
