package br.com.diameter.userservice.services;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.enums.OrderEnum;
import br.com.diameter.userservice.enums.SortEnum;
import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.GetUsersRequest;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.utils.UserUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaQuery<User> criteriaQuery;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Root<User> root;

    @Mock
    private TypedQuery<User> typedQuery;

    @Mock
    private UserUtils userUtils;

    @Test
    void shouldCreateUser_WhenPostUserObject() {
        var user = MockBuilder.createUser();
        var userRequest = MockBuilder.createUserRequest();
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(null);
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
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(user);
        var customException = Assertions.catchThrowable(() -> userService.createUser(userRequest));
        Assertions.assertThat(customException).isNotNull();
        Assertions.assertThat(customException).isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowException_WhenPasswordIsShorterCharacters() {
        var userRequest = new UserRequest("John Doe", "john.doe@mail.com", "1234");
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(null);
        var customException = Assertions.catchThrowable(() -> userService.createUser(userRequest));
        Assertions.assertThat(customException).isNotNull();
        Assertions.assertThat(customException).isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldGetAllUsers_WhenGetUsersWithoutParameters() {
        var user = MockBuilder.createUser();
        var emptyUserRequest = new GetUsersRequest(null, null, 0, 25, SortEnum.NAME, OrderEnum.ASC);
        var userResponse = MockBuilder.createUserResponse();
        var usersResponse = MockBuilder.createUsersResponse();
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        Mockito.when(criteriaQuery.from(User.class)).thenReturn(root);
        Mockito.when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).setMaxResults(ArgumentMatchers.anyInt())).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).setFirstResult(ArgumentMatchers.anyInt())).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).getResultList()).thenReturn(List.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Assertions.assertThat(userService.getUsers(emptyUserRequest)).isEqualTo(usersResponse);
    }

    @Test
    void shouldGetAllUsers_WhenGetUsersWithParametersAndDescOrdering() {
        var user = MockBuilder.createUser();
        var getUserRequest = new GetUsersRequest("John Doe", "john.doe@mail.com", 0, 25, SortEnum.NAME, OrderEnum.DESC);
        var userResponse = MockBuilder.createUserResponse();
        var usersResponse = MockBuilder.createUsersResponse();
        Mockito.when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        Mockito.when(criteriaBuilder.createQuery(User.class)).thenReturn(criteriaQuery);
        Mockito.when(criteriaQuery.from(User.class)).thenReturn(root);
        Mockito.when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).setMaxResults(ArgumentMatchers.anyInt())).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).setFirstResult(ArgumentMatchers.anyInt())).thenReturn(typedQuery);
        Mockito.when(entityManager.createQuery(criteriaQuery).getResultList()).thenReturn(List.of(user));
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Assertions.assertThat(userService.getUsers(getUserRequest)).isEqualTo(usersResponse);
    }

    @Test
    void shouldGetUserById_WhenSendUserIdWithPathParameter() {
        var uuid = UUID.fromString("c0f0e810-06a9-4bf5-828f-380f71d38a8b");
        var user = MockBuilder.createUser();
        Mockito.when(userRepository.findById(uuid)).thenReturn(Optional.ofNullable(user));
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Assertions.assertThat(userService.getUserById(uuid)).isEqualTo(userResponse);
    }

    @Test
    void shouldUpdateUser_WhenPutUserObject() {
        var userId = UUID.fromString("c0f0e810-06a9-4bf5-828f-380f71d38a8b");
        var user = MockBuilder.createUser();
        var userRequest = MockBuilder.createUserRequest();
        var userSaved = new User(userId, userRequest.name(), userRequest.email(), userRequest.password());
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        var userMapped = MockBuilder.createUser();
        Mockito.when(userRepository.save(userSaved)).thenReturn(userMapped);
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Assertions.assertThat(userService.updateUser(userId, userRequest)).isEqualTo(userResponse);
    }
}
