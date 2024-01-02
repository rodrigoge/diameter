package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.AuthenticationResponse;
import br.com.diameter.userservice.models.UserRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    public AuthenticationResponse login(UserRequest userRequest) {
        log.info("Starting the login user flow");
        log.info("Finding user by email into database");
        var user = userRepository.findByEmail(userRequest.email()).orElseThrow(() -> new BadRequestException("E-mail doesn't exists"));
        log.info("Mapping and authenticate user");
        var usernamePassword = new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        log.info("Generating user token flow");
        var token = tokenService.generateToken((User) auth.getPrincipal());
        log.info("Mapping user response will be returned");
        var userResponse = userMapper.toUserResponse(user);
        log.info("Finishing the login user flow");
        return AuthenticationResponse.builder().userResponse(userResponse).token(token).build();
    }
}
