package tcc.uff.resource.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tcc.uff.resource.server.exceptions.GenericException;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

@Service
@RequiredArgsConstructor
public class PreAuthorize {

    private final CourseService courseService;
    private final FrequencyServiceImpl frequencyService;

    public boolean isOwnerCourse(String name, String course) {
        if (courseService.isOwnerByUser(name, course)) return true;
        throw new GenericException("Voce não é dono desta Classe");
    }

    public boolean isMemberCourse(String name, String course) {
        if (courseService.isMemberByUser(name, course)) return true;
        throw new GenericException("Voce não participa desta Classe");
    }

    public boolean isOwnerCourseByFrequency(String name, String frequency){
        return frequencyService.isTeacherInFrequency(name, frequency);
    }
}
