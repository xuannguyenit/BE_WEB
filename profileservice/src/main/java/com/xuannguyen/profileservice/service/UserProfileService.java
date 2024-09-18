package com.xuannguyen.profileservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.xuannguyen.profileservice.dto.request.ProfileCreationRequest;
import com.xuannguyen.profileservice.dto.response.UserProfileResponse;
import com.xuannguyen.profileservice.entity.UserProfile;
import com.xuannguyen.profileservice.repository.UserProfileRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Service
@Slf4j
public class UserProfileService {
    @Autowired
    private UserProfileRepository userProfileRepository;
    //    @Autowired
    //    private UserProfileMapper userProfileMapper;
    // tạo mới 1 profile cho user
    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        //        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        //        userProfile = userProfileRepository.save(userProfile);
        //
        //        return userProfileMapper.toUserProfileReponse(userProfile);
        UserProfile userProfile = UserProfile.builder()
                .userid(request.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .city(request.getCity())
                .build();
        userProfileRepository.save(userProfile);
        return UserProfileResponse.builder()
                .id(userProfile.getId())
                .userId(userProfile.getUserid())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .dob(userProfile.getDob())
                .city(userProfile.getCity())
                .build();
    }

    public UserProfileResponse getProfile(String id) {
        //        UserProfile userProfile =
        //                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not
        // found"));
        //
        //
        //        return userProfileMapper.toUserProfileReponse(userProfile);
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new RuntimeException("User profile not found"));
        return UserProfileResponse.builder()
                .id(userProfile.getId())
                .userId(userProfile.getUserid())
                .firstName(userProfile.getFirstName())
                .lastName(userProfile.getLastName())
                .dob(userProfile.getDob())
                .city(userProfile.getCity())
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        List<UserProfile> listProfile = userProfileRepository.findAll();
        //        return profiles.stream().map(userProfileMapper::toUserProfileReponse).toList();
        List<UserProfileResponse> userProfileResponses = new ArrayList<>();
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        for (UserProfile userProfile : listProfile) {
            userProfileResponse.setId(userProfile.getId());
            userProfileResponse.setUserId(userProfile.getUserid());
            userProfileResponse.setFirstName(userProfile.getFirstName());
            userProfileResponse.setLastName(userProfile.getLastName());
            userProfileResponse.setDob(userProfile.getDob());
            userProfileResponse.setCity(userProfile.getCity());
            userProfileResponses.add(userProfileResponse);
        }
        return userProfileResponses;
    }
}
