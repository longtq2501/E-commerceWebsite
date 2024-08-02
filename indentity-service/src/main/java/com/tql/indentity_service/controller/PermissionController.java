package com.tql.indentity_service.controller;

import com.tql.indentity_service.dto.request.ApiResponse;
import com.tql.indentity_service.dto.request.PermissionRequest;
import com.tql.indentity_service.dto.response.PermissionResponse;
import com.tql.indentity_service.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
    PermissionService permissionService;

    @GetMapping("/get-all")
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @PostMapping("/create")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @DeleteMapping("/delete/{permisison}")
    ApiResponse<Void> delete(@PathVariable String permisison) {
        permissionService.delete(permisison);
        return ApiResponse.<Void>builder()
                .build();
    }
}
