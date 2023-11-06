package tcc.uff.resource.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreAuthorize {

    private final CourseService courseService;

    public boolean isOwnerCourse(String name, String course) {
        return courseService.isOwnerByUser(name, course);
    }
}
