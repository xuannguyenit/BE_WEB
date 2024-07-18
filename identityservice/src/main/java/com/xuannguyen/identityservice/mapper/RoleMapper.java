package com.xuannguyen.identityservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.xuannguyen.identityservice.dto.request.RoleRequest;
import com.xuannguyen.identityservice.dto.response.RoleResponse;
import com.xuannguyen.identityservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
