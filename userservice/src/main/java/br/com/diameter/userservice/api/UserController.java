package br.com.diameter.userservice.api;

import br.com.diameter.userservice.enums.OrderEnum;
import br.com.diameter.userservice.enums.SortEnum;
import br.com.diameter.userservice.models.GetUsersRequest;
import br.com.diameter.userservice.models.GetUsersResponse;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("Receiving request for create user");
        var response = userService.createUser(userRequest);
        log.info("Sending response and finishing create user flow");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetUsersResponse> getUsers(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String email,
                                                     @RequestParam(defaultValue = "0") int offset,
                                                     @RequestParam(defaultValue = "25") int limit,
                                                     @RequestParam(defaultValue = "NAME") SortEnum sort,
                                                     @RequestParam(defaultValue = "ASC") OrderEnum order) {
        log.info("Receiving request for get users");
        var request = new GetUsersRequest(name, email, offset, limit, sort, order);
        var response = userService.getUsers(request);
        log.info("Sending response and finishing get users flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID userId) {
        log.info("Receiving request for get user by id");
        var response = userService.getUserById(userId);
        log.info("Sending response and finishing get users flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable UUID userId, @Valid @RequestBody UserRequest userRequest) {
        log.info("Receiving request for update user by id");
        var response = userService.updateUser(userId, userRequest);
        log.info("Sending response and finishing update user flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        log.info("Receiving request for delete user by id");
        var response = userService.deleteUser(userId);
        log.info("Sending response and finishing delete user flow");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
