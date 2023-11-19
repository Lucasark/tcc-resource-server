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
public class UserAlias {

    private String name;

    /**
     * Embed
     */
    private String courseId;

}