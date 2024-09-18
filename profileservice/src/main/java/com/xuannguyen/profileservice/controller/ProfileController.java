package com.xuannguyen.profileservice.controller;

import java.util.List;

import com.xuannguyen.profileservice.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import com.xuannguyen.profileservice.dto.response.UserProfileResponse;
import com.xuannguyen.profileservice.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {

    UserProfileService userProfileService;
    // tạo profile mới
    // get profile theo id
    @GetMapping("/users/{profileId}")
    public UserProfileResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }
    // get all
    @GetMapping("/users/")
    public ApiResponse<List<UserProfileResponse>>getAllProfiles() {


        return ApiResponse.<List<UserProfileResponse>>builder()
                .result(userProfileService.getAllProfiles())
                .build();
    }
}
