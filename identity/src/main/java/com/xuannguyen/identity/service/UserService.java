package com.xuannguyen.identity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.xuannguyen.identity.constant.PredefinePermisstion;
import com.xuannguyen.identity.dto.request.*;
import com.xuannguyen.identity.dto.response.ProfileResponse;
import com.xuannguyen.identity.entity.Permission;
import com.xuannguyen.identity.repository.PermissionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.xuannguyen.event.dto.NotificationEvent;
import com.xuannguyen.identity.constant.PredefinedRole;
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
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    ProfileClient profileClient;
    KafkaTemplate<String, Object> kafkaTemplate;
// đăng ký user
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lấy role và thêm permission nếu tồn tại
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(userRole -> {
            HashSet<Permission> permissions = new HashSet<>();
            permissionRepository.findById(PredefinePermisstion.USER_PERMISSTION).ifPresent(permissions::add);

            // Debug kiểm tra xem permission đã thêm chưa
            System.out.println("Permissions found: " + permissions.size());

            userRole.setPermissions(permissions);
            roles.add(userRole);
        });

        user.setRoles(roles);


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
                .body("Username :" + request.getUsername() + "password: " + request.getPassword())
                .build();

        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);

        var userCreationReponse = userMapper.toUserResponse(user);

        userCreationReponse.setId(profile.getResult().getId());

        return userCreationReponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createAdminUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Lấy role và thêm permission nếu tồn tại
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.ADMIN_ROLE).ifPresent(userRole -> {
            HashSet<Permission> permissions = new HashSet<>();
            permissionRepository.findById(PredefinePermisstion.ADMIN_PERMISSTION).ifPresent(permissions::add);

            // Debug kiểm tra xem permission đã thêm chưa
            System.out.println("Permissions found: " + permissions.size());

            userRole.setPermissions(permissions);
            roles.add(userRole);
        });

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
                .body("Username :" + request.getUsername() + "password: " + request.getPassword())
                .build();

        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);

        var userCreationReponse = userMapper.toUserResponse(user);

        userCreationReponse.setId(profile.getResult().getId());

        return userCreationReponse;
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
//        String name = context.getAuthentication().getName();
//
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User user = userRepository.findById(userId).orElseThrow(()->new AppException(ErrorCode.USER_NOT_EXISTED));
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
    public User updateRoleUser(String userId, UpdateRoleUserRequest  request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Role role = roleRepository.findById(request.getRole().getName()).orElseThrow(()->new AppException(ErrorCode.ROLE_NOT_EXITED));
        if (role!=null){
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }
        return userRepository.save(user);
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
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUserAdmin (@RequestBody UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));


        HashSet<Role> roles = new HashSet<>();
        Role role = roleRepository.findById(PredefinedRole.ADMIN_ROLE).orElseThrow(()->new AppException(ErrorCode.ROLE_NOT_EXITED));
        Permission permission = permissionRepository.findById(PredefinePermisstion.ADMIN_PERMISSTION).orElseThrow(()->new AppException(ErrorCode.PERMISSION_NOT_EXITED));
        Set<Permission> permissions = new HashSet<>();
        permissions.add(permission);
        role.setPermissions(permissions);
        roles.add(role);
        user.setRoles(roles);
        user.setRoles(roles);
        user.setEmailVerified(true);

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
                .body("Username :" + request.getUsername() + "password: " + request.getPassword())
                .build();

        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);

        var userCreationReponse = userMapper.toUserResponse(user);

        userCreationReponse.setId(profile.getResult().getId());

        return userCreationReponse;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Integer getCountUsers() {
        return userRepository.getCountUser();
    }

}
