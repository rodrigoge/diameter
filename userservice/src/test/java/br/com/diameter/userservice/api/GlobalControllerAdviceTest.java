package br.com.diameter.userservice.api;

import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.exceptions.InternalServerErrorException;
import br.com.diameter.userservice.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class GlobalControllerAdviceTest {

    @InjectMocks
    private GlobalControllerAdvice globalControllerAdvice;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNotFoundException_WhenInvokeHandleException() {
        var exception = new NotFoundException("Error message from this test");
        var exceptionResponse = globalControllerAdvice.customRunTimeException(exception);
        Assertions.assertThat(exceptionResponse).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody()).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(exceptionResponse.getBody().getMessage()).isEqualTo(exception.getMessage());
        Assertions.assertThat(exceptionResponse.getBody().getDateTimeError()).isNotNull();
    }

    @Test
    void shouldReturnBadRequestException_WhenInvokeHandleException() {
        var exception = new BadRequestException("Error message from this test");
        var exceptionResponse = globalControllerAdvice.customBadRequestException(exception);
        Assertions.assertThat(exceptionResponse).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody()).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody().getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(exceptionResponse.getBody().getMessage()).isEqualTo(exception.getMessage());
        Assertions.assertThat(exceptionResponse.getBody().getDateTimeError()).isNotNull();
    }

    @Test
    void shouldReturnInternalServerErrorException_WhenInvokeHandleException() {
        var exception = new InternalServerErrorException("Error message from this test");
        var exceptionResponse = globalControllerAdvice.customInternalServerErrorException(exception);
        Assertions.assertThat(exceptionResponse).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody()).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody().getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(exceptionResponse.getBody().getMessage()).isEqualTo(exception.getMessage());
        Assertions.assertThat(exceptionResponse.getBody().getDateTimeError()).isNotNull();
    }

    @Test
    void shouldReturnRunTimeErrorException_WhenInvokeHandleException() {
        var exception = new RuntimeException("Error message from this test");
        var exceptionResponse = globalControllerAdvice.customRunTimeException(exception);
        Assertions.assertThat(exceptionResponse).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody()).isNotNull();
        Assertions.assertThat(exceptionResponse.getBody().getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(exceptionResponse.getBody().getMessage()).isEqualTo(exception.getMessage());
        Assertions.assertThat(exceptionResponse.getBody().getDateTimeError()).isNotNull();
    }
}
