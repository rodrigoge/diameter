package br.com.diameter.userservice.api;

import br.com.diameter.userservice.exceptions.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomException> customHandleException(CustomException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new CustomException(
                        exception.getHttpStatus(),
                        exception.getMessage(),
                        exception.getDateTimeError()
                ));
    }
}
