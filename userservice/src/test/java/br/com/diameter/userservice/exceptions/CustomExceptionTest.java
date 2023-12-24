package br.com.diameter.userservice.exceptions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class CustomExceptionTest {

    @InjectMocks
    private CustomException customException;

    @Test
    void shouldReturnCustomException_WhenToInvokeException() {
        var customException = new CustomException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error message in this test",
                LocalDateTime.now()
        );
        Assertions.assertThat(customException).isNotNull();
        Assertions.assertThat(customException.getHttpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        Assertions.assertThat(customException.getMessage()).isEqualTo("Error message in this test");
        Assertions.assertThat(customException.getDateTimeError()).isNotNull();
    }

}
