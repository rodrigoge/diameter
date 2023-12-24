package br.com.diameter.userservice.api;

import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.exceptions.InternalServerErrorException;
import br.com.diameter.userservice.exceptions.NotFoundException;
import br.com.diameter.userservice.exceptions.ErrorInfoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfoException> customRunTimeException(NotFoundException exception) {
        var errorInfoException = new ErrorInfoException(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(errorInfoException.getHttpStatus())
                .body(errorInfoException);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorInfoException> customBadRequestException(BadRequestException exception) {
        var errorInfoException = new ErrorInfoException(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(errorInfoException.getHttpStatus())
                .body(errorInfoException);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorInfoException> customInternalServerErrorException(InternalServerErrorException exception) {
        var errorInfoException = new ErrorInfoException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(errorInfoException.getHttpStatus())
                .body(errorInfoException);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfoException> customRunTimeException(RuntimeException exception) {
        var errorInfoException = new ErrorInfoException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(errorInfoException.getHttpStatus())
                .body(errorInfoException);
    }
}
