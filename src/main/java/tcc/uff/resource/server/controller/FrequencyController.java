package tcc.uff.resource.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.AttendanceRequest;
import tcc.uff.resource.server.model.request.FrequencyQueryRequest;
import tcc.uff.resource.server.model.response.FrequencyHandlerResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyResponse;
import tcc.uff.resource.server.service.impl.FrequencyHandlerServiceImpl;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.util.List;
import java.util.Objects;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/frequencies")
public class FrequencyController {

    private final FrequencyServiceImpl frequencyService;

    private final FrequencyHandlerServiceImpl frequencyHandlerService;

    @GetMapping("/courses/{courseId}/owner")
    @Operation(summary = "Frequencia de todo um curso pela data")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<List<FrequencyMapperResponse>> getQueryFrequency(Authentication authentication,
                                                                           @ParameterObject FrequencyQueryRequest frequencyQueryRequest,
                                                                           @PathVariable String courseId
    ) {
        if (Objects.nonNull(frequencyQueryRequest.getStart()) && Objects.nonNull(frequencyQueryRequest.getEnd())) {
            return ResponseEntity.ok(frequencyService.getFrequencies(courseId, frequencyQueryRequest.getStart(), frequencyQueryRequest.getEnd()));
        }
        return ResponseEntity.ok(frequencyService.getAllFrequencies(courseId));
    }

    @GetMapping("/courses/{courseId}/member")
    @Operation(summary = "Frequencia de membro de um curso")
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #courseId)")
    public ResponseEntity<List<FrequencyResponse>> getMemberFrequency(Authentication authentication,
                                                                      @PathVariable String courseId) {
        return ResponseEntity.ok(frequencyService.getAllFrequenciesOfMember(courseId, authentication.getName()));
    }

    @PostMapping("/courses/{courseId}/handler")
    @Operation(summary = "Cria uma Chamada para um Curso com Validador Estatico")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<FrequencyHandlerResponse> handlerAttendance(@Valid @RequestBody AttendanceRequest request,
                                                                      @PathVariable String courseId
    ) {
        return ResponseEntity.ok(frequencyHandlerService.handlerFrenquency(courseId, request));
    }

}
