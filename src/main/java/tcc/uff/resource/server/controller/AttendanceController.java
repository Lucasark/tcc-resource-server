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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.AttendancePatchRequest;
import tcc.uff.resource.server.model.response.AttendanceActivedResponse;
import tcc.uff.resource.server.service.impl.AttendanceServiceImpl;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendances")
public class AttendanceController {

    private final AttendanceServiceImpl attendanceService;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/courses/{courseId}/codes/{code}")
    @Operation(summary = "Incluir a Presença de um Curso pelo Código")
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #courseId)")
    public ResponseEntity<Void> updateFrequency(Authentication authentication,
                                @PathVariable String courseId,
                                @PathVariable String code
    ) {
        attendanceService.updateFrequency(courseId, code, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping("/frequencies/{frequencyId}/members/{memberId}")
    @Operation(summary = "Atualizar a Presença de um Curso de um Membro")
    //TODO: Otimizar
//    @PreAuthorize("@preAuthorize.isOwnerCourseByFrequency(authentication.name, #frequencyId)")
    public ResponseEntity<Void> updateAttedentceStatusByMember(Authentication authentication,
                                                               @Valid @RequestBody AttendancePatchRequest attendancePatchRequest,
                                                               @PathVariable String frequencyId,
                                                               @PathVariable String memberId
    ) {
        attendanceService.updateAttedentceStatusByMember(frequencyId, memberId, attendancePatchRequest.getStatus());
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @GetMapping("/courses/{courseId}/actived")
    @Operation(summary = "Valida se existe uma chamada em Andamento")
    public ResponseEntity<AttendanceActivedResponse> frequencyIsActived(@PathVariable String courseId) {
        return ResponseEntity.ok(attendanceService.isActived(courseId));
    }


}
