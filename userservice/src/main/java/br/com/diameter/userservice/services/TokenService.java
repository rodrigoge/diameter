package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.mappers.UserMapper;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Log4j2
public class TokenService {

    @Value("${token.secret}")
    private String tokenSecret;

    private UserMapper userMapper;

    public String generateToken(User user) {
        log.info("Starting the generating token flow");
        try {
            log.info("Getting token algorithm");
            var algorithm = Algorithm.HMAC256(tokenSecret);
            log.info("Creating token object and sending response");
            return JWT.create()
                    .withIssuer("diameter-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    private Instant generateExpirationDate() {
        log.info("Generating expiration date token");
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token) {
        log.info("Starting the validation token flow");
        try {
            log.info("Getting token algorithm");
            var algorithm = Algorithm.HMAC256(tokenSecret);
            log.info("Validating token object and sending response");
            return JWT.require(algorithm)
                    .withIssuer("diameter-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error while verification token", exception);
        }
    }
}
