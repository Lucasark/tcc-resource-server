package tcc.uff.resource.server.model.response.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse implements Serializable {

    private String id;

    private String name;

    private String period;

    @Builder.Default
    private Set<DaysOfWeekResponse> daysOfWeek = new HashSet<>();

    private String about;

    private CurseTeacherResponse teacher;

    @Builder.Default
    private Set<CurseMemberResponse> members = new HashSet<>();

    private CourseLocationResponse location;

}
