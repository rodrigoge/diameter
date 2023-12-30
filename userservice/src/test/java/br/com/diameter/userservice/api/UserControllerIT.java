package br.com.diameter.userservice.api;

import br.com.diameter.userservice.builders.MockBuilder;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.models.GetUsersResponse;
import br.com.diameter.userservice.models.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;
import java.util.UUID;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static HttpHeaders httpHeaders;

    @BeforeAll
    static void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldCreateUser_WhenIntegrationTestUserController() throws JsonProcessingException {
        var userRequest = MockBuilder.createUserRequest();
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
        Assertions.assertEquals(responseBody.email(), "john.doe@mail.com");
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
        var usersList = MockBuilder.createUsersResponse();
        var responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertEquals(responseBody.users(), usersList.users());
        Assertions.assertEquals(responseBody.totalNumberOfRecords(), usersList.totalNumberOfRecords());
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
}
