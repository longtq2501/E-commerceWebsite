package com.tql.indentity_service.controller;

import com.tql.indentity_service.dto.request.ApiResponse;
import com.tql.indentity_service.dto.request.RoleRequest;
import com.tql.indentity_service.dto.response.RoleResponse;
import com.tql.indentity_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @GetMapping("get-all")
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @PostMapping("create")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @DeleteMapping("delete/{name}")
    void delete(@PathVariable String name) {
        roleService.delete(name);
    }

    @PutMapping("update/{name}")
    ApiResponse<RoleResponse> update(@RequestBody RoleRequest request, @PathVariable String name) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.update(request, name))
                .build();
    }
}
