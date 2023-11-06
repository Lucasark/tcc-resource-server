package tcc.uff.resource.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.CourseQueryRequest;
import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.response.entity.CourseResponse;
import tcc.uff.resource.server.service.CourseService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/owner")
    public ResponseEntity<List<CourseResponse>> getAllCoursesOwnerByAuth(Authentication authentication) {
        return ResponseEntity.ok(courseService.getAllCourserOwnerByUser(authentication.getName()));
    }

    @GetMapping("/member")
    public ResponseEntity<List<CourseResponse>> getAllCoursesMemberByAuth(Authentication authentication) {
        return ResponseEntity.ok(courseService.getAllCourserMemberByUser(authentication.getName()));
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<CourseResponse> getInfoCourse(Authentication authentication,
                                                        @PathVariable("courseId") String courseId) {
        return ResponseEntity.ok(CourseResponse.builder().id(courseId).build());
    }


    @PatchMapping("/{courseId}")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void patchCourse(Authentication authentication,
                            @RequestBody CourseQueryRequest request,
                            @PathVariable String courseId) {
        //TODO PATCH!! --> APERTOU O QUADRADO
    }

    @PutMapping()
    public ResponseEntity<CourseResponse> createCourse(Authentication authentication,
                                                       @RequestBody @Valid CourseRequest request) {

        var a = courseService.createCourse(request, authentication.getName());

        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/{courseId}")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public void deleteCourse(Authentication authentication,
                             @PathVariable String courseId,
                             @RequestBody CourseQueryRequest request) {
        //TODO DELETE!! --> APERTOU NA LIXEIRA, MATA O CURSO
    }


}
