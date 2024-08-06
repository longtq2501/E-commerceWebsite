package com.tql.profile_service.mapper;

import com.tql.profile_service.dto.request.UserProfileRequest;
import com.tql.profile_service.dto.response.UserProfileResponse;
import com.tql.profile_service.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(UserProfileRequest request);
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    UserProfile toUpdateUserProfile(@MappingTarget UserProfile userProfile, UserProfileRequest request);
}
