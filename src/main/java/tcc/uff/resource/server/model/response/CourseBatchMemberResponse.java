package tcc.uff.resource.server.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseBatchMemberResponse {

    @Builder.Default
    private Set<UserAddResponse> failed = new HashSet<>();

}
