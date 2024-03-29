package tcc.uff.resource.server.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.CourseRequest;
import tcc.uff.resource.server.model.request.UserAddInBatchRequest;
import tcc.uff.resource.server.model.request.UserAddRequest;
import tcc.uff.resource.server.model.response.CourseBatchMemberResponse;
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
    @Operation(summary = "Pegar todos so cursos que sou Dono")
    public ResponseEntity<List<CourseResponse>> getAllCoursesOwnerByAuth(Authentication authentication) {
        return ResponseEntity.ok(courseService.getAllCourserOwnerByUser(authentication.getName()));
    }

    @GetMapping("/member")
    @Operation(summary = "Pegar todos so cursos que sou Membro")
    public ResponseEntity<List<CourseResponse>> getAllCoursesMemberByAuth(Authentication authentication) {
        return ResponseEntity.ok(courseService.getAllCourserMemberByUser(authentication.getName()));
    }

    @GetMapping("/{courseId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Pegar a Info de um curso")
    public ResponseEntity<CourseResponse> getInfoCourse(Authentication authentication,
                                                        @PathVariable("courseId") String courseId) {
        return ResponseEntity.ok(courseService.getCourse(courseId));
    }


    @PutMapping("/{courseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Atualizar Campos de Informação do Curso, ex: About")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<CourseResponse> putCourse(Authentication authentication,
                                                      @RequestBody CourseRequest request,
                                                      @PathVariable String courseId
    ) {
        return ResponseEntity.ok(courseService.putCourse(request, courseId));
    }

    @PostMapping
    @Operation(summary = "Criar um curso")
    public ResponseEntity<CourseResponse> createCourse(Authentication authentication,
                                                       @RequestBody @Valid CourseRequest request
    ) {
        return ResponseEntity.ok(courseService.createCourse(request, authentication.getName()));
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Deletar um curso")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public void deleteCourse(Authentication authentication,
                             @PathVariable String courseId
    ) {
        courseService.deleteCourse(courseId);
    }


    @PatchMapping("/{courseId}/member")
    @Operation(summary = "Adicionar um membro ao Curso - Obrigatorio ser o Dono")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<CourseResponse> addMember(Authentication authentication,
                                                    @RequestBody @Valid UserAddRequest userAddRequest,
                                                    @PathVariable String courseId
    ) {
        return ResponseEntity.ok(courseService.addMember(courseId, userAddRequest.getEmail(), userAddRequest.getAlias(), userAddRequest.getRegistration()));
    }

    @PatchMapping("/{courseId}/member-batch")
    @Operation(summary = "Adicionar pacote de membros ao Curso - Obrigatorio ser o Dono ")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<CourseBatchMemberResponse> addBatchMember(Authentication authentication,
                                                                    @RequestBody @Valid UserAddInBatchRequest userAddInBatchRequest,
                                                                    @PathVariable String courseId
    ) {
        return ResponseEntity.ok(courseService.addMembers(userAddInBatchRequest, courseId));
    }
}
