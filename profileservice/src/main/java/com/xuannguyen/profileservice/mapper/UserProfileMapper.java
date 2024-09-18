package com.xuannguyen.profileservice.mapper;

import org.mapstruct.Mapper;

import com.xuannguyen.profileservice.dto.request.ProfileCreationRequest;
import com.xuannguyen.profileservice.dto.response.UserProfileResponse;
import com.xuannguyen.profileservice.entity.UserProfile;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfile toUserProfile(ProfileCreationRequest request);

    UserProfileResponse toUserProfileReponse(UserProfile entity);
}
