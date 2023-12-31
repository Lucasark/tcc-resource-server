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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.AttendencePatchRequest;
import tcc.uff.resource.server.service.impl.AttendenceServiceImpl;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendences")
public class AttendenceController {

    private final AttendenceServiceImpl attendenceService;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/courses/{courseId}/codes/{code}")
    @Operation(summary = "Incluir a Presença de um Curso pelo Código")
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #courseId)")
    public ResponseEntity<Void> updateFrequency(Authentication authentication,
                                @PathVariable String courseId,
                                @PathVariable String code
    ) {
        attendenceService.updateFrequency(courseId, code, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/frequencies/{frequencyId}/members/{memberId}")
    @Operation(summary = "Atualizar a Presença de um Curso de um Membro")
    @PreAuthorize("@preAuthorize.isOwnerCourseByFrequency(authentication.name, #frequencyId)")
    public ResponseEntity<Void> updateAttedentceStatusByMember(Authentication authentication,
                                                               @Valid @RequestBody AttendencePatchRequest attendencePatchRequest,
                                                               @PathVariable String frequencyId,
                                                               @PathVariable String memberId
    ) {
        attendenceService.updateAttedentceStatusByMember(frequencyId, memberId, attendencePatchRequest.getStatus());
        return ResponseEntity.ok().build();
    }

}
