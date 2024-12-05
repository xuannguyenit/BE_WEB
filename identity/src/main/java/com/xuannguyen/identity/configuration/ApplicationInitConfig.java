package com.xuannguyen.identity.configuration;

import java.util.HashSet;

import com.xuannguyen.identity.constant.PredefinePermisstion;
import com.xuannguyen.identity.entity.Permission;
import com.xuannguyen.identity.repository.PermissionRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.xuannguyen.identity.constant.PredefinedRole;
import com.xuannguyen.identity.entity.Role;
import com.xuannguyen.identity.entity.User;
import com.xuannguyen.identity.repository.RoleRepository;
import com.xuannguyen.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";


    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Permission permissionUser = permissionRepository.save(
                        Permission.builder()
                                .name(PredefinePermisstion.USER_PERMISSTION)
                                .description("User permission")
                                .build()
                );

                Permission permissionAdmin = permissionRepository.save(
                        Permission.builder()
                                .name(PredefinePermisstion.ADMIN_PERMISSTION)
                                .description("Admin full permission")
                                .build()
                );

                Role userRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .build());
                var userPermisstion = new HashSet<Permission>();
                userPermisstion.add(permissionUser);
                userRole.setPermissions(userPermisstion);

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var permissionsAdmin = new HashSet<Permission>();
                permissionsAdmin.add(permissionAdmin);
                adminRole.setPermissions(permissionsAdmin);
                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .emailVerified(true)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
