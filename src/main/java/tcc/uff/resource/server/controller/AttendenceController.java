package tcc.uff.resource.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.AttendenceRequest;
import tcc.uff.resource.server.service.impl.AttendenceServiceImpl;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/attendences")
public class AttendenceController {

    private final AttendenceServiceImpl attendenceService;

    @PostMapping("/courses/{courseId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("@preAuthorize.isOwnerCourse(authentication.name, #courseId)")
    public Object createAttendence(@Valid @RequestBody AttendenceRequest request,
                                   @PathVariable String courseId
    ) {
        return attendenceService.createAttendence(courseId, request.getDate());
    }
}
