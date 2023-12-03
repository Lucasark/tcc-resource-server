package tcc.uff.resource.server.model.document;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private CourseDocument course;

    private Instant date;

    @Builder.Default
    private Set<Attendance> attendances = new HashSet<>();

    @Builder.Default
    private Boolean finished = Boolean.FALSE;

    private LocalDateTime finishedAt;

    @Version
    private Long version;

    @CreatedDate
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

}
