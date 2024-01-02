package br.com.diameter.userservice.api;

import br.com.diameter.userservice.models.AuthenticationResponse;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.services.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserRequest userRequest) {
        log.info("Receiving request for login user");
        var response = loginService.login(userRequest);
        log.info("Sending response and finishing login user flow");
        return ResponseEntity.ok(response);
    }
}
