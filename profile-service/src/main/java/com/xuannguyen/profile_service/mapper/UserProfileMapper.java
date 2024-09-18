package com.xuannguyen.profile_service.mapper;

import org.mapstruct.Mapper;

import com.xuannguyen.profile_service.dto.request.ProfileCreationRequest;
import com.xuannguyen.profile_service.dto.response.UserProfileResponse;
import com.xuannguyen.profile_service.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);
}
