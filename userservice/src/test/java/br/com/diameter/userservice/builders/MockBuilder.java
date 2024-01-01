package br.com.diameter.userservice.builders;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.enums.OrderEnum;
import br.com.diameter.userservice.enums.SortEnum;
import br.com.diameter.userservice.models.GetUsersRequest;
import br.com.diameter.userservice.models.GetUsersResponse;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;

import java.util.List;
import java.util.UUID;

public class MockBuilder {

    public static User createUser() {
        return User
                .builder()
                .id(UUID.fromString("c0f0e810-06a9-4bf5-828f-380f71d38a8b"))
                .name("John Doe")
                .email("john.doe@mail.com")
                .password("123456789")
                .build();
    }

    public static UserRequest createUserRequest() {
        return new UserRequest("John Doe", "john.doe@mail.com", "12345678");
    }

    public static UserRequest updateUserRequest() {
        return new UserRequest("John Doe", "john.doe@email.com", "12345678");
    }

    public static UserResponse createUserResponse() {
        return new UserResponse("John Doe", "john.doe@mail.com");
    }

    public static GetUsersRequest createUsersRequest() {
        return new GetUsersRequest("John Doe", "john.doe@mail.com", 0, 25, SortEnum.NAME, OrderEnum.ASC);
    }

    public static GetUsersResponse createUsersResponse() {
        var userResponse = new UserResponse("John Doe", "john.doe@mail.com");
        return new GetUsersResponse(List.of(userResponse), 1);
    }
}
