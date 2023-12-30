package br.com.diameter.userservice.api;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.enums.OrderEnum;
import br.com.diameter.userservice.enums.SortEnum;
import br.com.diameter.userservice.services.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        var response = userController.createUser(userRequest);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().name()).isEqualTo(userResponse.name());
        Assertions.assertThat(response.getBody().email()).isEqualTo(userResponse.email());
    }

    @Test
    void shouldGetUsers_WhenUnitTestUserController() {
        var request = MockBuilder.createUsersRequest();
        var response = MockBuilder.createUsersResponse();
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userService.getUsers(request)).thenReturn(response);
        var usersResponse = userController.getUsers("John Doe", "john.doe@mail.com", 0, 25, SortEnum.NAME, OrderEnum.ASC);
        Assertions.assertThat(usersResponse.getBody()).isNotNull();
        Assertions.assertThat(usersResponse.getBody().users()).isEqualTo(List.of(userResponse));
        Assertions.assertThat(usersResponse.getBody().totalNumberOfRecords()).isEqualTo(1);
    }

    @Test
    void shouldGetUserById_WhenUnitTestUserController() {
        var userId = MockBuilder.createUser().getId();
        var response = MockBuilder.createUserResponse();
        Mockito.when(userService.getUserById(userId)).thenReturn(response);
        var usersResponse = userController.getUserById(userId);
        Assertions.assertThat(usersResponse.getBody()).isNotNull();
        Assertions.assertThat(usersResponse.getBody().name()).isEqualTo(response.name());
        Assertions.assertThat(usersResponse.getBody().email()).isEqualTo(response.email());
    }

    @Test
    void shouldUpdateUserById_When_UnitTestUserController() {
        var userId = MockBuilder.createUser().getId();
        var userRequest = MockBuilder.createUserRequest();
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userService.updateUser(userId, userRequest)).thenReturn(userResponse);
        var response = userController.updateUser(userId, userRequest);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().name()).isEqualTo(userResponse.name());
        Assertions.assertThat(response.getBody().email()).isEqualTo(userResponse.email());
    }
}
