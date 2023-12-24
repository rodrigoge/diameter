package br.com.diameter.userservice.exceptions;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ErrorsExceptionTest {

    @InjectMocks
    private NotFoundException notFoundException;

    @Test
    void shouldReturnNotFoundException_WhenToInvokeException() {
        var notFoundException = new NotFoundException("Error message in this test");
        Assertions.assertThat(notFoundException).isNotNull();
        Assertions.assertThat(notFoundException.getMessage()).isEqualTo("Error message in this test");
    }

    @Test
    void shouldReturnBadRequestException_WhenToInvokeException() {
        var badRequestException = new BadRequestException("Error message in this test");
        Assertions.assertThat(badRequestException).isNotNull();
        Assertions.assertThat(badRequestException.getMessage()).isEqualTo("Error message in this test");
    }

    @Test
    void shouldReturnInternalServerErrorException_WhenToInvokeException() {
        var internalServerError = new InternalServerErrorException("Error message in this test");
        Assertions.assertThat(internalServerError).isNotNull();
        Assertions.assertThat(internalServerError.getMessage()).isEqualTo("Error message in this test");
    }
}
