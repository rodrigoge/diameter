package br.com.diameter.userservice.api;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.filters.AuthenticationFilter;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.GetUsersResponse;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.services.TokenService;
import br.com.diameter.userservice.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Mock
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    private static HttpHeaders httpHeaders;

    @BeforeAll
    static void init() {
        var algorithm = Algorithm.HMAC256("7A6B6851696D734E30593263357638792B423F4528482B4D6251655368566D59");
        var bearerToken = JWT.create()
                .withIssuer("diameter-api")
                .withSubject("john.doe@mail.com")
                .withExpiresAt(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + bearerToken);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUser_WhenIntegrationTestUserController() throws IOException {
        var user = MockBuilder.createUser();
        var userRequest = MockBuilder.updateUserRequest();
        var userResponse = MockBuilder.createUserResponse();
        Mockito.when(userRepository.findByEmail(userRequest.email())).thenReturn(null);
        Mockito.when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        Mockito.when(userService.createUser(userRequest)).thenReturn(userResponse);
        Mockito.when(tokenService.validateToken(ArgumentMatchers.anyString())).thenReturn("john.doe@mail.com");
        Mockito.when(userRepository.findByEmail("john.doe@mail.com")).thenReturn(null);
        var entity = new HttpEntity<>(objectMapper.writeValueAsString(userRequest), httpHeaders);
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users",
                HttpMethod.POST,
                entity,
                UserResponse.class
        );
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        var responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertEquals(responseBody.name(), "John Doe");
        Assertions.assertEquals(responseBody.email(), "john.doe@email.com");
    }

    @Test
    void shouldGetUsers_WhenIntegrationTestUserController() {
        var entity = new HttpEntity<>(null, httpHeaders);
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users",
                HttpMethod.GET,
                entity,
                GetUsersResponse.class
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        var responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertNotNull(responseBody.users());
        Assertions.assertNotNull(responseBody.totalNumberOfRecords());
    }

    @Test
    void shouldGetUserById_WhenIntegrationTestUserController() {
        var userId = UUID.randomUUID();
        var entity = new HttpEntity<>(null, httpHeaders);
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users/" + userId,
                HttpMethod.GET,
                entity,
                UserResponse.class
        );
        var responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertNull(responseBody.name());
        Assertions.assertNull(responseBody.email());
    }

    @Test
    void shouldUpdateUserById_WhenIntegrationTestUserController() throws JsonProcessingException {
        var userId = UUID.randomUUID();
        var userRequest = MockBuilder.createUserRequest();
        var entity = new HttpEntity<>(objectMapper.writeValueAsString(userRequest), httpHeaders);
        var response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users/" + userId,
                HttpMethod.PUT,
                entity,
                UserResponse.class
        );
        var responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertNull(responseBody.name());
        Assertions.assertNull(responseBody.email());
    }

    @Test
    void shouldDeleteUserById_WhenIntegrationTestUserController() {
        var user = MockBuilder.createUser();
        var userId = user.getId();
        userRepository.save(user);
        var entity = new HttpEntity<>(null, httpHeaders);
        testRestTemplate.exchange(
                "http://localhost:" + port + "/api/v1/users/" + userId,
                HttpMethod.DELETE,
                entity,
                String.class
        );
        Assertions.assertEquals(userRepository.findById(user.getId()), Optional.of(user));
    }
}
