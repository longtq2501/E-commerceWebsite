package com.tql.indentity_service.service;

import com.tql.indentity_service.dto.request.PermissionRequest;
import com.tql.indentity_service.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
     PermissionResponse create(PermissionRequest request);
     List<PermissionResponse> getAll();
     void delete(String permission);
}
