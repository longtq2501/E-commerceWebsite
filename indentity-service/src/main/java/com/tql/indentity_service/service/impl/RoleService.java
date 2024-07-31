package com.tql.indentity_service.service.impl;

import com.tql.indentity_service.dto.request.RoleRequest;
import com.tql.indentity_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String name);
}
