package tcc.uff.resource.server.model.response.entity;

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
public class CourseResponse {

    private String id;

    private String name;

    private String period;

    private Set<DaysOfWeekResponse> daysOfWeeks;

    private String about;

    private String owner;

    @Builder.Default
    private Set<UserResponse> members = new HashSet<>();

}
