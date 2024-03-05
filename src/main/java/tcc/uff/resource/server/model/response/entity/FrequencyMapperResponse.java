package tcc.uff.resource.server.model.response.entity;

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
public class FrequencyMapperResponse {

    private String id;

    private String alias;

    private String registration;

    @Builder.Default
    private Set<FrequencyResponse> frequencies = new HashSet<>();
}
