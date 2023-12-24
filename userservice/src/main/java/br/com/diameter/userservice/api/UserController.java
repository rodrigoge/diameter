package br.com.diameter.userservice.api;

import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
