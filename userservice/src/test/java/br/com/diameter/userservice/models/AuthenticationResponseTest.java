package br.com.diameter.userservice.models;

import br.com.diameter.userservice.builders.MockBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticationResponseTest {

    @InjectMocks
    private AuthenticationResponse authenticationResponse;

    @Test
    void shouldCreateAuthenticationResponse() {
        var userResponse = MockBuilder.createUserResponse();
        var authenticationResponse = AuthenticationResponse
                .builder()
                .userResponse(userResponse)
                .token("12345678")
                .build();
        Assertions.assertThat(authenticationResponse).isNotNull();
        Assertions.assertThat(authenticationResponse.getUserResponse()).isNotNull();
        Assertions.assertThat(authenticationResponse.getToken()).isNotNull();
    }

    @Test
    void shouldCreateAuthenticationResponse_WithNoArgsConstructor() {
        var authenticationResponse = new AuthenticationResponse();
        Assertions.assertThat(authenticationResponse).isNotNull();
    }
}
