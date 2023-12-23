package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.CustomException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.utils.UserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    public UserResponse createUser(UserRequest userRequest) {
        log.info("Starting the create user flow");
        var emailExists = userRepository.findByEmail(userRequest.email());
        if(emailExists.isPresent()) {
            throw new CustomException(
                    HttpStatus.BAD_REQUEST,
                    "E-mail already exists",
                    LocalDateTime.now()
            );
        }
        userUtils.validatePasswordLength(userRequest.password());
        log.info("Mapping and saving user request into database");
        var user = userMapper.toUser(userRequest);
        userUtils.encryptPassword(user);
        var userSaved = userRepository.save(user);
        log.info("Mapping user response will be returned");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("Finishing the create user flow");
        return userResponse;
    }
}
