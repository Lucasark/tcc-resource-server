package tcc.uff.resource.server.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;
import tcc.uff.resource.server.model.enums.UserContactEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserContact {

    @Field("id")
    private UserContactEnum id;

    private String value;
}