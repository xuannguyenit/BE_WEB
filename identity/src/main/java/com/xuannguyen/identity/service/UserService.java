package com.xuannguyen.identity.service;

import java.util.HashSet;
import java.util.List;

import com.xuannguyen.identity.dto.request.ApiResponse;
import com.xuannguyen.identity.dto.request.ProfileCreationRequest;
import com.xuannguyen.identity.dto.response.ProfileResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xuannguyen.event.dto.NotificationEvent;
import com.xuannguyen.identity.constant.PredefinedRole;
import com.xuannguyen.identity.dto.request.UserCreationRequest;
import com.xuannguyen.identity.dto.request.UserUpdateRequest;
import com.xuannguyen.identity.dto.response.UserResponse;
import com.xuannguyen.identity.entity.Role;
import com.xuannguyen.identity.entity.User;
import com.xuannguyen.identity.exception.AppException;
import com.xuannguyen.identity.exception.ErrorCode;
import com.xuannguyen.identity.mapper.ProfileMapper;
import com.xuannguyen.identity.mapper.UserMapper;
import com.xuannguyen.identity.repository.RoleRepository;
import com.xuannguyen.identity.repository.UserRepository;
import com.xuannguyen.identity.repository.httpclient.ProfileClient;

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
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<Role> roles = new HashSet<>();

        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user.setEmailVerified(false);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getId());

        var profile = profileClient.createProfile(profileRequest);

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("EMAIL")
                .recipient(request.getEmail())
                .subject("Welcome to xuannguyen")
                .body("Hello, " + request.getUsername())
                .build();

        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);

        var userCreationReponse = userMapper.toUserResponse(user);

        userCreationReponse.setId(profile.getResult().getId());

        return userCreationReponse;
    }

//public UserResponse createUser(UserCreationRequest request) {
//    User user = userMapper.toUser(request);
//    user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//    HashSet<Role> roles = new HashSet<>();
//    roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
//
//    user.setRoles(roles);
//
//    try {
//        user = userRepository.save(user);
//    } catch (DataIntegrityViolationException exception){
//        throw new AppException(ErrorCode.USER_EXISTED);
//    }
//
////        ServletRequestAttributes servletRequestAttributes =
////                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
////        var authHeader= servletRequestAttributes.getRequest().getHeaders("Authorization");
//
//    // tạo profile
//    ProfileCreationRequest profileCreationRequest = ProfileCreationRequest.builder()
//            .userId(user.getId())
//            .firstName(request.getFirstName())
//            .lastName(request.getLastName())
//            .city(request.getCity())
//            .dob(request.getDob())
//            .build();
//    ApiResponse<ProfileResponse> profileResponse = profileClient.createProfile(profileCreationRequest);
////    NotificationEvent notificationEvent = NotificationEvent.builder()
////            .channel("EMAIL")
////            .recipient(request.getEmail())
////            .subject("Welcome to My Shop")
////            .body("Hello, " + request.getUsername())
////            .build();
////    kafkaTemplate.send("notification-delivery",notificationEvent);
//
//    return userMapper.toUserResponse(user);
//}

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }
}
