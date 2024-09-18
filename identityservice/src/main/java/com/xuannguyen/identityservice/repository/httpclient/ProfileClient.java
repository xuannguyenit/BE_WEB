package com.xuannguyen.identityservice.repository.httpclient;

import com.xuannguyen.identityservice.configuration.AuthenticationRequestInterceptor;
import com.xuannguyen.identityservice.dto.request.ApiResponse;
import com.xuannguyen.identityservice.dto.request.ProfileCreationRequest;
import com.xuannguyen.identityservice.dto.response.ProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient (name = "profileservice",url = "${app.service.profile}",
    configuration = {AuthenticationRequestInterceptor.class} )
public interface ProfileClient {
    @PostMapping(value = "/profiles/intenal/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<ProfileResponse> createProfile (@RequestBody ProfileCreationRequest request);

    @GetMapping(value ="/profiles/users/", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<ProfileResponse>>getAllProfiles();

//    @GetMapping(value ="/profiles/users/{profileId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    ApiResponse<ProfileResponse> getProfile (@PathVariable String profileId);

}
