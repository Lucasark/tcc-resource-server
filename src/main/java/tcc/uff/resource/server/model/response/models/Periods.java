package tcc.uff.resource.server.model.response.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tcc.uff.resource.server.model.response.SelectOption;

import java.util.List;

@Getter
@Setter
@Builder
public class Periods {

    private List<SelectOption> periods;

}
