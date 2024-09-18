package com.xuannguyen.profileservice.controller;

import com.xuannguyen.profileservice.dto.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import com.xuannguyen.profileservice.dto.request.ProfileCreationRequest;
import com.xuannguyen.profileservice.dto.response.UserProfileResponse;
import com.xuannguyen.profileservice.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IntenalProfileController {

    UserProfileService userProfileService;
    // tạo profile mới
    @PostMapping("/intenal/users")
    public ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request) {
//        return userProfileService.createProfile(request);
        return ApiResponse.<UserProfileResponse>builder()
                .result(userProfileService.createProfile(request))
                .build();
    }
}
