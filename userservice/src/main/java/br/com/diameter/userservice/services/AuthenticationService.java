package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.JwtAuthenticationResponse;
import br.com.diameter.userservice.models.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    public JwtAuthenticationResponse signIn(UserRequest userRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequest.email(), userRequest.password()));
        var user = userRepository.findByEmail(userRequest.email()).orElseThrow(() -> new BadRequestException("Invalid e-mail or password"));
        var jwt = jwtService.generateToken(user);
        var userResponse = userMapper.toUserResponse(user);
        return JwtAuthenticationResponse.builder().userResponse(userResponse).token(jwt).build();
    }
}
