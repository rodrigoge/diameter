package br.com.diameter.userservice.api;

import br.com.diameter.userservice.models.JwtAuthenticationResponse;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody UserRequest userRequest) {
        var response = authenticationService.signIn(userRequest);
        return ResponseEntity.ok(response);
    }
}
