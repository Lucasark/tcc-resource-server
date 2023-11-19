package tcc.uff.resource.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreAuthorize {

    private final CourseService courseService;

    public boolean isOwnerCourse(String name, String course) {
        if (courseService.isOwnerByUser(name, course)) return true;
        throw new RuntimeException("Voce não é dono desta Classe");
    }

    public boolean isMemberCourse(String name, String course) {
        if (courseService.isMemberByUser(name, course)) return true;
        throw new RuntimeException("Voce não participa desta Classe");
    }
}
