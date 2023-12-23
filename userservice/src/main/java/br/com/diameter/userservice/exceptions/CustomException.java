package br.com.diameter.userservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime dateTimeError;
}
