package tcc.uff.resource.server.model.response.relation.teachercourse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tcc.uff.resource.server.model.document.DaysOfWeek;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class Course {

    private String id;

    private String name;

    private String period;

    @Builder.Default
    private Set<DaysOfWeek> daysOfWeeks = new HashSet<>();

    private String about;

}
