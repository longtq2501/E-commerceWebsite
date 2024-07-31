package com.tql.indentity_service.service;

import com.tql.indentity_service.dto.request.RoleRequest;
import com.tql.indentity_service.dto.response.RoleResponse;
import com.tql.indentity_service.entity.Permission;
import com.tql.indentity_service.entity.Role;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.exception.AppException;
import com.tql.indentity_service.mapper.RoleMapper;
import com.tql.indentity_service.repository.PermissionRepository;
import com.tql.indentity_service.repository.RoleRepository;
import com.tql.indentity_service.service.impl.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        if(roleRepository.existsByName(request.getName())){
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        var permission = permissionRepository.findAllById(request.getPermissions());

        var role = roleMapper.toRole(request);

        role.setPermissions(new HashSet<>(permission));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll() {
        var roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String name) {
        if(!roleRepository.existsByName(name)){
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
        roleRepository.deleteById(name);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse update(RoleRequest request, String name) {
        if(!roleRepository.existsByName(name)){
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }
        var permission = permissionRepository.findAllById(request.getPermissions());

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(new HashSet<>(permission))
                .build();
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
}
