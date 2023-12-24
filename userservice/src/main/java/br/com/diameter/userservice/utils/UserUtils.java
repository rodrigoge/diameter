package br.com.diameter.userservice.utils;

import br.com.diameter.userservice.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void encryptPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
