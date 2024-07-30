package com.tql.indentity_service.service;

import com.tql.indentity_service.dto.request.UserRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.enums.AppException;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.enums.Role;
import com.tql.indentity_service.mapper.UserMapper;
import com.tql.indentity_service.repository.UserRepository;
import com.tql.indentity_service.service.impl.UserService;
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
import org.springframework.util.CollectionUtils;

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

    @Override
    public UserResponse create(UserRequest request) {
        if (userRepository.existsUserByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        var user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(request.getConfirmPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        user.setRoles(roles);

        var saved = userRepository.save(user);
        return userMapper.toUserResponse(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        log.info("Get all users");
        var users = userRepository.findAll();
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getById(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse update (UserRequest request, String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        var updatedUser = userMapper.updateUser(user, request);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
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
