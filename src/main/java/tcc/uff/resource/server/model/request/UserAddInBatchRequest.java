package tcc.uff.resource.server.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddInBatchRequest {

    @NotEmpty
    @Builder.Default
    private Set<UserAddRequest> members = new HashSet<>();

}
