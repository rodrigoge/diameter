package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.BadRequestException;
import br.com.diameter.userservice.exceptions.NotFoundException;
import br.com.diameter.userservice.mappers.UserMapper;
import br.com.diameter.userservice.models.GetUsersRequest;
import br.com.diameter.userservice.models.GetUsersResponse;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import br.com.diameter.userservice.utils.UserUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private EntityManager entityManager;

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
        log.info("Verifying if e-mail already exists");
        if (userWithEmailVerified != null && !userWithEmailVerified.getEmail().isEmpty()) {
            throw new BadRequestException("E-mail already exists");
        }
    }

    private void verifyPasswordLength(UserRequest user) {
        log.info("Verifying password length");
        if (user != null && user.password() != null && user.password().length() < 8) {
            throw new BadRequestException("Password shorter than 8 characters");
        }
    }

    public GetUsersResponse getUsers(GetUsersRequest request) {
        log.info("Starting the get users flow");
        var users = buildParams(request);
        var usersResponse = mapperToResponse(users);
        var response = new GetUsersResponse(usersResponse, usersResponse.size());
        log.info("Finishing the get users flow");
        return response;
    }

    private List<User> buildParams(GetUsersRequest request) {
        log.info("Building request parameters to get users");
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(User.class);
        var root = criteriaQuery.from(User.class);
        log.info("Building predicates with request");
        var predicates = new ArrayList<Predicate>();
        if (StringUtils.hasText(request.name())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.name().toLowerCase() + "%"));
        }
        if (StringUtils.hasText(request.email())) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + request.email().toLowerCase() + "%"));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        orderingFetchUsers(request, criteriaBuilder, criteriaQuery, root);
        log.info("Finishing request parameters to get users");
        return entityManager
                .createQuery(criteriaQuery)
                .setMaxResults(request.limit())
                .setFirstResult(request.offset() * request.limit())
                .getResultList();
    }

    private List<UserResponse> mapperToResponse(List<User> users) {
        log.info("Mapping get users response");
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    private void orderingFetchUsers(GetUsersRequest request, CriteriaBuilder criteriaBuilder, CriteriaQuery<User> criteriaQuery, Root<User> root) {
        log.info("Ordering fetch users request");
        if(request.orderEnum() != null) {
            if("ASC".equalsIgnoreCase(request.orderEnum().getDescription())) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
            }
            if("DESC".equalsIgnoreCase(request.orderEnum().getDescription())) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
            }
        }
    }

    public UserResponse getUserById(UUID userId) {
        log.info("Starting the get user by id flow");
        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found in database"));
        log.info("Mapping user response will be returned");
        var userResponse = userMapper.toUserResponse(user);
        log.info("Finishing the create user flow");
        return userResponse;
    }

    @Transactional
    public UserResponse updateUser(UUID userId, UserRequest userRequest) {
        log.info("Starting the update user flow");
        var optionalUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found in database"));
        if(!userRequest.email().equals(optionalUser.getEmail())) {
            var userWithEmailVerified = userRepository.findByEmail(userRequest.email());
            verifyEmailAlreadyExists(userWithEmailVerified);
        }
        verifyPasswordLength(userRequest);
        log.info("Setting and updating user request into database");
        var user = mapUserToUpdate(userId, userRequest, optionalUser);
        userUtils.encryptPassword(user);
        var userSaved = userRepository.save(user);
        log.info("Mapping user response will be returned");
        var userResponse = userMapper.toUserResponse(userSaved);
        log.info("Finishing the update user flow");
        return userResponse;
    }

    private User mapUserToUpdate(UUID userId, UserRequest userRequest, User user) {
        var name = userRequest.name() == null || userRequest.name().isEmpty() ? user.getName() : userRequest.name();
        var email = userRequest.email() == null || userRequest.email().isEmpty() ? user.getEmail() : userRequest.email();
        var password = userRequest.password() == null || userRequest.password().isEmpty() ? user.getPassword() : userRequest.password();
        return new User(userId, name, email, password);
    }
}
