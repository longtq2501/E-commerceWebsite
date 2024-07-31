package com.tql.indentity_service.mapper;

import com.tql.indentity_service.dto.request.UserCreateRequest;
import com.tql.indentity_service.dto.request.UserUpdateRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    User toUpdateUser(@MappingTarget User user, UserUpdateRequest request);
}
