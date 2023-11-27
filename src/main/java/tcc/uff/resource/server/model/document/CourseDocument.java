package tcc.uff.resource.server.model.document;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

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

    private String name;

    private String period;

    /**
     * Is it not relatated to Frequency, is it just info about
     */
    @Builder.Default
    private Set<DaysOfWeek> daysOfWeek = new HashSet<>();

    private String about;

    @DBRef
    @NotNull
    private UserDocument teacher;

    @DBRef
    @Builder.Default
    private Set<UserDocument> members = new HashSet<>();


    //Embed
    @Builder.Default
    private Set<String> frequencies = new HashSet<>();

    @Version
    private Long version;

}
