package br.com.diameter.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorInfoException {

    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime dateTimeError;
}
