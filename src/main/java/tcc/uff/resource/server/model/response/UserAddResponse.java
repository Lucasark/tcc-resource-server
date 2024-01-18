package tcc.uff.resource.server.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import tcc.uff.resource.server.model.UserAddBase;

import java.io.Serial;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserAddResponse extends UserAddBase {

    @Serial
    private static final long serialVersionUID = -1433639046301700329L;

    @NonNull
    private String reason;

}
