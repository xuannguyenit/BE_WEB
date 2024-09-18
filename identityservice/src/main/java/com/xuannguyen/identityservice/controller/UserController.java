package com.xuannguyen.identityservice.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xuannguyen.identityservice.dto.request.ApiResponse;
import com.xuannguyen.identityservice.dto.request.UserCreationRequest;
import com.xuannguyen.identityservice.dto.request.UserUpdateRequest;
import com.xuannguyen.identityservice.dto.response.UserResponse;
import com.xuannguyen.identityservice.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/registration")
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

//    @GetMapping("/list")
//    ApiResponse<List<UserResponse>> getUsers() {
//        return ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build();
//    }
@GetMapping("/list")
public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers() {
    try {
        // Gọi phương thức getUsers() từ UserService
        ApiResponse<List<UserResponse>> userResponse = userService.getUsers();

        // Trả về phản hồi thành công với mã trạng thái HTTP 200
        return ResponseEntity.ok(userResponse);
    } catch (Exception e) {
        // Ghi log lỗi
        log.error("Error fetching users", e);

        // Tạo phản hồi lỗi với mã trạng thái HTTP 500
        ApiResponse<List<UserResponse>> errorResponse = ApiResponse.<List<UserResponse>>builder()
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(userId))
                .build();
    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }
}
