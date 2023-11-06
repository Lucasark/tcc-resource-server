package tcc.uff.resource.server.model.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeAlias("course")
@Document(collection = "course")
public class CourseDocument {

    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    @Indexed(unique = true)
    private String name;

    private String period;

    /**
     * Is it not relatated to Frequency, is it just info about
     */
    @Builder.Default
    private Set<LocalDateTime> daysOfWeeks = new HashSet<>();

    private String about;

    @DBRef
    private UserDocument teacher;

    @DBRef
    @Builder.Default
    private Set<UserDocument> members = new HashSet<>();

    @DBRef(lazy = true)
    @Builder.Default
    private Set<FrequencyDocument> frequencies = new HashSet<>();

}
