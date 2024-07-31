package com.tql.indentity_service.service.impl;

import com.tql.indentity_service.dto.request.UserCreateRequest;
import com.tql.indentity_service.dto.request.UserUpdateRequest;
import com.tql.indentity_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    List<UserResponse> getAll();
    UserResponse getById(String id);
    UserResponse update(UserUpdateRequest request, String id);
    void delete(String id);
    UserResponse getMyInfo();
}
