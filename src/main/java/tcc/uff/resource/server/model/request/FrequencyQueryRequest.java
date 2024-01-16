package tcc.uff.resource.server.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyQueryRequest {

    @NotNull(message = "Deve informar o começo")
    private Instant start;

    @NotNull(message = "Deve informar o final")
    private Instant end;

}
