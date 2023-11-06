package tcc.uff.resource.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @GetMapping
    public ResponseEntity<Void> getTeacher(Authentication authentication) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teachers/include/courses")
    public ResponseEntity<Void> getTeacherCourses(Authentication authentication) {
        return ResponseEntity.noContent().build();
    }


}
