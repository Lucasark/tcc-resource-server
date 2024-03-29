package tcc.uff.resource.server.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tcc.uff.resource.server.model.UserAddBase;

import java.io.Serial;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserAddRequest extends UserAddBase {

    @Serial
    private static final long serialVersionUID = 1565477176858377103L;

}
