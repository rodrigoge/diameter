package br.com.diameter.userservice.services;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.db.UserRepository;
import br.com.diameter.userservice.exceptions.BadRequestException;
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
        if (userWithEmailVerified != null && !userWithEmailVerified.getEmail().isEmpty()) {
            throw new BadRequestException("E-mail already exists");
        }
    }

    private void verifyPasswordLength(UserRequest user) {
        if (user != null && user.password() != null && user.password().length() < 8) {
            throw new BadRequestException("Password shorter than 8 characters");
        }
    }

    public GetUsersResponse getUsers(GetUsersRequest request) {
        var users = buildParams(request);
        var usersResponse = mapperToResponse(users);
        return new GetUsersResponse(usersResponse, usersResponse.size());
    }

    private List<User> buildParams(GetUsersRequest request) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(User.class);
        var root = criteriaQuery.from(User.class);
        var predicates = new ArrayList<Predicate>();
        if (StringUtils.hasText(request.name())) {
            predicates.add(criteriaBuilder.equal(root.get("name"), request.name()));
        }
        if (StringUtils.hasText(request.email())) {
            predicates.add(criteriaBuilder.equal(root.get("email"), request.email()));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        orderingFetchUsers(request, criteriaBuilder, criteriaQuery, root);
        return entityManager
                .createQuery(criteriaQuery)
                .setMaxResults(request.limit())
                .setFirstResult(request.offset() * request.limit())
                .getResultList();
    }

    private List<UserResponse> mapperToResponse(List<User> users) {
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    private void orderingFetchUsers(GetUsersRequest request, CriteriaBuilder criteriaBuilder, CriteriaQuery<User> criteriaQuery, Root<User> root) {
        if(request.orderEnum() != null) {
            if("ASC".equalsIgnoreCase(request.orderEnum().getDescription())) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get(request.orderEnum().getDescription())));
            }
            if("DESC".equalsIgnoreCase(request.orderEnum().getDescription())) {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get(request.orderEnum().getDescription())));
            }
        }
    }
}
