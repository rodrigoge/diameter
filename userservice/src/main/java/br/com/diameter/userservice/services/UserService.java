package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Starting the create user flow");
        var userWithEmailVerified = userRepository.findByEmail(userRequest.email());
        verifyEmailAlreadyExists(userWithEmailVerified);
        verifyPasswordLength(userRequest);
        log.info("Mapping and saving user request into database");
        var user = userMapper.toUser(userRequest);
        userUtils.encryptPassword(user);
        var userSaved = userRepository.save(user);
        log.info("Mapping user response will be returned");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("Finishing the create user flow");
        return userResponse;
    }

    private void verifyEmailAlreadyExists(User userWithEmailVerified) {
        if (userWithEmailVerified != null && !userWithEmailVerified.getEmail().isEmpty()) {
            throw new BadRequestException("E-mail already exists");
        }
    }

    private void verifyPasswordLength(UserRequest user) {
        if (user != null && user.password() != null && user.password().length() < 8) {
            throw new BadRequestException("Password shorter than 8 characters");
        }
    }
}
