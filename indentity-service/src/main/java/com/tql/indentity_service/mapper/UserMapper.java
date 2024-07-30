package com.tql.indentity_service.mapper;

import com.tql.indentity_service.dto.request.UserRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request);
    UserResponse toUserResponse(User user);

    User updateUser(@MappingTarget User user, UserRequest request);
}
