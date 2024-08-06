package com.tql.indentity_service.mapper;

import com.tql.indentity_service.dto.request.ProfileCreationRequest;
import com.tql.indentity_service.dto.request.UserCreateRequest;
import org.mapstruct.Mapper;

@Mapper
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreateRequest request);
}
