package tcc.uff.resource.server.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tcc.uff.resource.server.exceptions.TempException;
import tcc.uff.resource.server.model.response.ErrorResponse;

@Slf4j
@ControllerAdvice
public class ControllerAdviceExceptionHandler {

    @ExceptionHandler(value = {TempException.class})
    public ResponseEntity<ErrorResponse> handleException(TempException eThrowable) {

        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT)
                .body(ErrorResponse.builder()
                        .message("TESTE - MESSAGE")
                        .description("TESTE - UTF - ã - Ã - É - é")
                        .build()
                );
    }

}
