package com.tql.indentity_service.service.impl;

import com.tql.indentity_service.dto.request.UserRequest;
import com.tql.indentity_service.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);
    List<UserResponse> getAll();
    UserResponse getById(String id);
    UserResponse update(UserRequest request, String id);
    void delete(String id);
    UserResponse getMyInfo();
}
