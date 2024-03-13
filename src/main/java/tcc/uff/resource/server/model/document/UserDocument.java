package tcc.uff.resource.server.model.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@TypeAlias("User")
@Document(collection = "users")
public class UserDocument {

    @MongoId(targetType = FieldType.STRING)
    private String email;

    /**
     * OAuth
     */
    private String name;

    /**
     * Se vier do:
     * <p>
     * - OAuth
     * - Dono do User altearando
     * - Pela Planilha
     */
    private String registration;

    /**
     * O que vem da planilha
     */
    @Builder.Default
    private Set<UserAlias> aliases = new HashSet<>();

    private UserInfo info;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}