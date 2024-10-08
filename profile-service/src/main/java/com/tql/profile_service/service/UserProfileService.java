package com.tql.profile_service.service;

import com.tql.profile_service.dto.request.UserProfileRequest;
import com.tql.profile_service.dto.response.UserProfileResponse;

import java.util.List;

public interface UserProfileService {
    UserProfileResponse createProfile(UserProfileRequest request);

    UserProfileResponse updateProfile(String id, UserProfileRequest request);

    List<UserProfileResponse> getAll();
}
