package tcc.uff.resource.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tcc.uff.resource.server.model.request.AttendenceRequest;
import tcc.uff.resource.server.model.request.FrequencyQueryRequest;
import tcc.uff.resource.server.model.response.FrequencyCreateResponse;
import tcc.uff.resource.server.model.response.entity.FrequencyMapperResponse;
import tcc.uff.resource.server.service.impl.FrequencyServiceImpl;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/frequencies")
public class FrequencyController {

    private final FrequencyServiceImpl frequencyService;

    @GetMapping("/courses/{courseId}/owner")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<List<FrequencyMapperResponse>> getQueryFrequency(Authentication authentication,
                                                                           FrequencyQueryRequest frequencyQueryRequest,
                                                                           @PathVariable String courseId
    ) {
        return ResponseEntity.ok(frequencyService.getFrequencies(courseId, frequencyQueryRequest.getStart(), frequencyQueryRequest.getEnd()));
    }

    @GetMapping("/courses/{courseId}/member")
    @PreAuthorize("@preAuthorize.isMemberCourse(authentication.name, #courseId)")
    public Object getMemberFrequency(Authentication authentication,
                                     @PathVariable String courseId) {
        //TODO GET PARA PEGAR A FREQUENCIA DE UM MEMBRO PELO MEMBRO;
        return null;
    }

    @PostMapping("/courses/{courseId}")
    @Operation(summary = "Cria uma Chamada para um Curso com Validador Estatico")
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public ResponseEntity<FrequencyCreateResponse> createAttendence(@Valid @RequestBody AttendenceRequest request,
                                                                    @PathVariable String courseId
    ) {
        return ResponseEntity.ok(frequencyService.initFrenquency(courseId, request.getDate()));
    }


}
