package tcc.uff.resource.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import tcc.uff.resource.server.model.request.UserPatchInfoRequest;
import tcc.uff.resource.server.model.response.entity.UserResponse;
import tcc.uff.resource.server.service.UserService;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getInfoUser(Authentication authentication) {
        return ResponseEntity.ok(userService.getInfoUser(authentication.getName()));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> updateInfoUser(
            Authentication authentication,
            @RequestBody UserPatchInfoRequest userPatchRequest
    ) {
        return ResponseEntity.ok(userService.updateInfoUser(authentication.getName(), userPatchRequest));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> getInfoUser(
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(userService.getInfoUser(userId));
    }

}
