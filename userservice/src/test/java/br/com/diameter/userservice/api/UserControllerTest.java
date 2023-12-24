package br.com.diameter.userservice.api;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Test
    void shouldCreateUser_WhenUnitTestUserController() {
        var userRequest = MockBuilder.createUserRequest();
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userService.createUser(userRequest)).thenReturn(userResponse);
        var response = userService.createUser(userRequest);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.name()).isEqualTo(userResponse.name());
        Assertions.assertThat(response.email()).isEqualTo(userResponse.email());
    }
}
