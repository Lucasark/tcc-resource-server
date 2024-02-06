package tcc.uff.resource.server.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tcc.uff.resource.server.model.enums.UserContactEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserContact {

    private UserContactEnum type;

    private String value;
}