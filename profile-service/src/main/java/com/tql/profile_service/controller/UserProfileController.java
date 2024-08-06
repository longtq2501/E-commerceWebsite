package com.tql.profile_service.controller;

import com.tql.profile_service.dto.request.ApiResponse;
import com.tql.profile_service.dto.request.UserProfileRequest;
import com.tql.profile_service.dto.response.UserProfileResponse;
import com.tql.profile_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/create")
    ApiResponse<UserProfileResponse> createProfile(@RequestBody UserProfileRequest request) {
        var userProfile = userProfileService.createProfile(request);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfile)
                .build();
    }

    @PutMapping("/update/{id}")
    ApiResponse<UserProfileResponse> updateProfile(@PathVariable String id, @RequestBody UserProfileRequest request) {
        var userProfile = userProfileService.updateProfile(id, request);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfile)
                .build();
    }
}
