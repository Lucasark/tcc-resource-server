package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tcc.uff.resource.server.model.enums.UserContactEnum;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchContactRequest implements Serializable {

    private UserContactEnum type;

    private String value;

}
