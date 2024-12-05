package com.xuannguyen.identity.dto.request;

import com.xuannguyen.identity.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleUserRequest {
    private Role role;
}
