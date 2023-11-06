package tcc.uff.resource.server.model.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private String id;

    private String name;

    private String period;

    private Set<LocalDateTime> daysOfWeeks;

    private String about;

    private String owner;

}
