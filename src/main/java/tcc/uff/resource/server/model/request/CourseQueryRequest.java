package tcc.uff.resource.server.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CourseQueryRequest {

    private String id;

}
