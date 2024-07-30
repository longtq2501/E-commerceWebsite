package com.tql.indentity_service.controller;

import com.tql.indentity_service.dto.request.ApiResponse;
import com.tql.indentity_service.dto.request.UserRequest;
import com.tql.indentity_service.dto.response.UserResponse;
import com.tql.indentity_service.service.impl.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping("/create")
    ApiResponse<UserResponse> create(@RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.create(request))
                .build();
    }

    @GetMapping("/get-all")
    ApiResponse<List<UserResponse>> getAll() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAll())
                .build();
    }

    @GetMapping("/get-by-id/{id}")
    ApiResponse<UserResponse> getById(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getById(id))
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<UserResponse> update (@RequestBody UserRequest request, @PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.update(request, id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<Void> delete (@PathVariable String id) {
        userService.delete(id);
        return ApiResponse.<Void>builder()
                .build();
    }
}
