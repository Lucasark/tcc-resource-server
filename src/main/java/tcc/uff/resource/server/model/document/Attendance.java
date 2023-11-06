package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@Builder
public class Attendance {

    @DBRef
    private UserDocument student;

    private Integer status;

}
