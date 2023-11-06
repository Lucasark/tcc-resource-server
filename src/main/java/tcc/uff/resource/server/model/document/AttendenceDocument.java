package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Getter
@Setter
@Builder
@TypeAlias("attendence")
@Document(collection = "attendence")
public class AttendenceDocument {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    @DBRef
    @Indexed(unique = true)
    private CourseDocument course;

    private String code;

    private Instant date;

    @Builder.Default
    private Integer repeat = 0;


}
