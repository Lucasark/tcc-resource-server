package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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