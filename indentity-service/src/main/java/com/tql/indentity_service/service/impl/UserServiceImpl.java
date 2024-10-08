package com.tql.indentity_service.service.impl;

import com.tql.indentity_service.dto.request.UserCreateRequest;
import com.tql.indentity_service.dto.request.UserUpdateRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.entity.Role;
import com.tql.indentity_service.entity.User;
import com.tql.indentity_service.exception.AppException;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.mapper.ProfileMapper;
import com.tql.indentity_service.mapper.UserMapper;
import com.tql.indentity_service.repository.RoleRepository;
import com.tql.indentity_service.repository.UserRepository;
import com.tql.indentity_service.repository.httpclient.ProfileClient;
import com.tql.indentity_service.service.UserService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    ProfileClient profileClient;
    ProfileMapper profileMapper;

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        var user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));

        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        user.setRoles(roles);

        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());

        var profileResponse = profileClient.createProfile(profileRequest);

        log.info(profileResponse.toString());

        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        log.info("Get all users");
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    @PreAuthorize("hasAuthority('READ_DATA')")
    public UserResponse getById(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    @PreAuthorize("hasAuthority('UPDATE_DATE')")
    public UserResponse update(UserUpdateRequest request, String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User updatedUser = userMapper.toUpdateUser(user, request);
        HashSet<Role> roles = new HashSet<>();
        roles.add(roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)));
        user.setRoles(roles);
        return userMapper.toUserResponse(userRepository.save(updatedUser));
    }

    @Override
    @PreAuthorize("hasAuthority('DELETE_DATA')")
    public void delete(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!userRepository.existsUserByUsername(authentication.getName())) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        else {
            var user = userRepository.findByUsername(authentication.getName());
            return userMapper.toUserResponse(user);
        }
    }
}
