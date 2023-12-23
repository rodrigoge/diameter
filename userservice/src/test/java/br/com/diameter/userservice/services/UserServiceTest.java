package br.com.diameter.userservice.services;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.CustomException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.utils.UserUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserUtils userUtils;

    @Test
    void shouldCreateUser() {
        var user = MockBuilder.createUser();
        var userRequest = MockBuilder.createUserRequest();
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
        Mockito.when(userMapper.toUser(userRequest)).thenReturn(user);
        var userMapped = MockBuilder.createUser();
        Mockito.when(userRepository.save(user)).thenReturn(userMapped);
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Assertions.assertThat(userService.createUser(userRequest)).isEqualTo(userResponse);
    }

    @Test
    void shouldThrowException_WhenEmailAlreadyExists() {
        var user = MockBuilder.createUser();
        var userRequest = MockBuilder.createUserRequest();
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));
        var customException = Assertions.catchThrowable(() -> userService.createUser(userRequest));
        Assertions.assertThat(customException).isNotNull();
        Assertions.assertThat(customException).isInstanceOf(CustomException.class);
    }
}
