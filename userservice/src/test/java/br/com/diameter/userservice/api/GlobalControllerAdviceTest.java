package br.com.diameter.userservice.api;

import br.com.diameter.userservice.exceptions.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class GlobalControllerAdviceTest {

    @InjectMocks
    private GlobalControllerAdvice globalControllerAdvice;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldGlobalHandleException() {
        var handleException = new CustomException(
                HttpStatus.BAD_REQUEST,
                "Error message from this test",
                LocalDateTime.now()
        );
        var customExceptionResponse = globalControllerAdvice.customHandleException(handleException);
        Assertions.assertThat(customExceptionResponse).isNotNull();
        Assertions.assertThat(customExceptionResponse.getBody()).isNotNull();
        Assertions.assertThat(customExceptionResponse.getBody().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(customExceptionResponse.getBody().getMessage()).isEqualTo(handleException.getMessage());
        Assertions.assertThat(customExceptionResponse.getBody().getDateTimeError()).isEqualTo(handleException.getDateTimeError());
    }
}
