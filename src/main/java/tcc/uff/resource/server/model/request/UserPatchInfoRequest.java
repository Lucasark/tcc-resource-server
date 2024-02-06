package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchInfoRequest implements Serializable {

    private String about;

    @Builder.Default
    private Set<UserPatchContactRequest> contacts = new HashSet<>();

}
