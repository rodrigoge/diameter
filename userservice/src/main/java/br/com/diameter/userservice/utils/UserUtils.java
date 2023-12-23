package br.com.diameter.userservice.utils;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserUtils {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void validatePasswordLength(String password) {
        if (password.length() < 8) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "Password shorter than 8 characters",
                    LocalDateTime.now()
            );
        }
    }

    public void encryptPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
