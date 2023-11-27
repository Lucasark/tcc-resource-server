package tcc.uff.resource.server.model.document;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@TypeAlias("frequency")
@Document(collection = "frequency")
@CompoundIndex(def = "{ 'course.id': 1, 'date': 1 }", unique = true)
public class FrequencyDocument {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    @DBRef
    @NotNull
    @Indexed(unique = true)
    private CourseDocument course;

    @Indexed(background = true)
    private Instant date;

    @Builder.Default
    private Set<Attendance> attendances = new HashSet<>();

    @Version
    private Long version;

}
