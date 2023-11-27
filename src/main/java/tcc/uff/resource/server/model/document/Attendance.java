package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import tcc.uff.resource.server.model.enums.AttendenceEnum;

@Getter
@Setter
@Builder
public class Attendance {

    @DBRef
    private UserDocument student;

    @Builder.Default
    private AttendenceEnum status = AttendenceEnum.MISS;

}
