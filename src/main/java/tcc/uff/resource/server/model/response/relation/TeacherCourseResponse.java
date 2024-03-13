package tcc.uff.resource.server.model.response.relation;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class TeacherCourseResponse {

    //TODO
    @Builder.Default
    private Set<?> courses = new HashSet<>();


}
