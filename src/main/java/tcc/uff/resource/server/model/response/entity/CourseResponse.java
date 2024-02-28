package tcc.uff.resource.server.model.response.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static tcc.uff.resource.server.utils.Constants.DATE_TIME_PATTERN;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdAt;

}
