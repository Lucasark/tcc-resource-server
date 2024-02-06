package tcc.uff.resource.server.model.response.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tcc.uff.resource.server.model.response.GenericOption;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class UserContact implements Serializable {

    private List<GenericOption> contacts;
}
