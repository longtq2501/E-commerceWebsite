package com.tql.indentity_service.service;

import com.tql.indentity_service.dto.request.UserRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.enums.AppException;
import com.tql.indentity_service.enums.ErrorCode;
import com.tql.indentity_service.mapper.UserMapper;
import com.tql.indentity_service.repository.UserRepository;
import com.tql.indentity_service.service.impl.UserService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        var user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var save = userRepository.save(user);
        return userMapper.toUserResponse(save);
    }

    @Override
    public List<UserResponse> getAll() {
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
}
