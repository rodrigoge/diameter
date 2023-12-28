package br.com.diameter.userservice.api;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

    @Test
    void shouldGetUsers_WhenUnitTestUserController() {
        var request = MockBuilder.createUsersRequest();
        var response = MockBuilder.createUsersResponse();
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userService.getUsers(request)).thenReturn(response);
        var usersResponse = userService.getUsers(request);
        Assertions.assertThat(usersResponse).isNotNull();
        Assertions.assertThat(usersResponse.users()).isEqualTo(List.of(userResponse));
        Assertions.assertThat(usersResponse.totalNumberOfRecords()).isEqualTo(1);
    }
}
