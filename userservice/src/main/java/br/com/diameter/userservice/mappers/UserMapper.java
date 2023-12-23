package br.com.diameter.userservice.mappers;

import br.com.diameter.userservice.db.User;
import br.com.diameter.userservice.models.UserRequest;
import br.com.diameter.userservice.models.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toUser(UserRequest userRequest);

    UserResponse toUserResponse(User user);
}
