package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@TypeAlias("User")
@Document(collection = "users")
public class UserDocument {

    @Id
    private String email;

    private String name;
}