package com.tql.profile_service.service.impl;

import com.tql.profile_service.dto.request.UserProfileRequest;
import com.tql.profile_service.dto.response.UserProfileResponse;
import com.tql.profile_service.entity.UserProfile;
import com.tql.profile_service.mapper.UserProfileMapper;
import com.tql.profile_service.repository.UserProfileRepository;
import com.tql.profile_service.service.UserProfileService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    @Override
    public UserProfileResponse createProfile(UserProfileRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile.setUserId(request.getUserId());
        userProfile =  userProfileRepository.save(userProfile);
        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @Override
    public UserProfileResponse updateProfile(String id, UserProfileRequest request) {
        var user = userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        var userProfileResponse = userProfileMapper.toUpdateUserProfile(user, request);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfileResponse));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAll() {
        var profiles = userProfileRepository.findAll();
        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }
}
