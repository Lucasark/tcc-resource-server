package tcc.uff.resource.server.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class CourseRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String period;

    @Builder.Default
    private Set<DaysOfWeekRequest> daysOfWeeks = new HashSet<>();

    private String about;

}
