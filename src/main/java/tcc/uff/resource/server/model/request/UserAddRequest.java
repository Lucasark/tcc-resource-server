package tcc.uff.resource.server.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddRequest {

    @NotBlank
    private String email;

    private String registration;

    @NotBlank
    private String alias;

}
