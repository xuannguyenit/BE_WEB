package com.xuannguyen.identityservice.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.xuannguyen.event.dto.NotificationEvent;
import com.xuannguyen.identityservice.dto.request.ApiResponse;
import com.xuannguyen.identityservice.dto.request.ProfileCreationRequest;
import com.xuannguyen.identityservice.dto.response.PermissionResponse;
import com.xuannguyen.identityservice.dto.response.ProfileResponse;
import com.xuannguyen.identityservice.dto.response.RoleResponse;
import com.xuannguyen.identityservice.repository.httpclient.ProfileClient;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.xuannguyen.identityservice.constant.PredefinedRole;
import com.xuannguyen.identityservice.dto.request.UserCreationRequest;
import com.xuannguyen.identityservice.dto.request.UserUpdateRequest;
import com.xuannguyen.identityservice.dto.response.UserResponse;
import com.xuannguyen.identityservice.entity.Role;
import com.xuannguyen.identityservice.entity.User;
import com.xuannguyen.identityservice.exception.AppException;
import com.xuannguyen.identityservice.exception.ErrorCode;
import com.xuannguyen.identityservice.mapper.UserMapper;
import com.xuannguyen.identityservice.repository.RoleRepository;
import com.xuannguyen.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    KafkaTemplate<String,Object> kafkaTemplate;
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

//        ServletRequestAttributes servletRequestAttributes =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        var authHeader= servletRequestAttributes.getRequest().getHeaders("Authorization");

        // tạo profile
        ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
                .userId(user.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .city(request.getCity())
                .dob(request.getDob())
                .build();
        ApiResponse<ProfileResponse> profileResponse = profileClient.createProfile(profileCreationRequest);
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to My Shop")
                .body("Hello, " + request.getUsername())
                .build();
        kafkaTemplate.send("notification-delivery",notificationEvent);

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    public ApiResponse<List<UserResponse>> getUsers() {
//        log.info("In method get Users");
//        List<UserResponse> userResponseList = new ArrayList<>();
//        ApiResponse<List<ProfileResponse>> response = profileClient.getAllProfiles();// gọi về từ feign client
//        List<ProfileResponse> profileResponseList = response.getResult();
//
//        List<User> users = userRepository.findAll();
//        for (ProfileResponse profileResponse : profileResponseList) {
//            for (User user : users) {
//
//                if (user.getId().equals(profileResponse.getUserId())) {
//
//                    PermissionResponse permissionResponse = new PermissionResponse();
//                    Set<Role> roles = user.getRoles();
//                    Set<RoleResponse> roleResponses = new HashSet<>();
//                    RoleResponse roleResponse = new RoleResponse();
//                    for (Role role : roles) {
//                        roleResponse.setName(role.getName());
//                        roleResponses.add(roleResponse);
//                    }
//
//                    userResponseList.add(UserResponse.builder()
//                            .username(user.getUsername())
//                            .firstName(profileResponse.getFirstName())
//                            .lastName(profileResponse.getLastName())
//                            .dob(profileResponse.getDob())
//                            .roles(roleResponses)
//                            .build());
//                }
//            }
//        }
//        return ApiResponse.<List<UserResponse>>builder()
//                .result(userResponseList)
//                .build();
//    }
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<UserResponse>> getUsers() {
        log.info("In method get Users");
        List<UserResponse> userResponseList = new ArrayList<>();

        try {
            ApiResponse<List<ProfileResponse>> profileResponseApi = profileClient.getAllProfiles();
            List<ProfileResponse> profileResponseList = profileResponseApi.getResult();

            if (profileResponseList == null) {
            profileResponseList = Collections.emptyList();
            }

            Map<String, ProfileResponse> profileMap = profileResponseList.stream()
                    .collect(Collectors.toMap(ProfileResponse::getUserId, Function.identity()));
            List<User> users = userRepository.findAll();

            for (User user : users) {
            ProfileResponse profileResponse = profileMap.get(user.getId());

            if (profileResponse != null) {
                Set<RoleResponse> roleResponses = user.getRoles().stream()
                        .map(role -> RoleResponse.builder().name(role.getName()).build())
                        .collect(Collectors.toSet());

                UserResponse userResponse = UserResponse.builder()
                        .username(user.getUsername())
                        .firstName(profileResponse.getFirstName())
                        .lastName(profileResponse.getLastName())
                        .dob(profileResponse.getDob())
                        .roles(roleResponses)
                        .build();

                userResponseList.add(userResponse);
            }
        }
        } catch (Exception e) {
            log.error("Error fetching profiles or users", e);
            // Xử lý lỗi phù hợp, có thể trả về lỗi
            return ApiResponse.<List<UserResponse>>builder()
                    .message("Unable to fetch users")
                    .build();
        }

    return ApiResponse.<List<UserResponse>>builder()
            .result(userResponseList)
            .build();
}


    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
