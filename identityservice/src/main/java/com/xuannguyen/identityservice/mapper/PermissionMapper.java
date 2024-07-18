package com.xuannguyen.identityservice.mapper;

import org.mapstruct.Mapper;

import com.xuannguyen.identityservice.dto.request.PermissionRequest;
import com.xuannguyen.identityservice.dto.response.PermissionResponse;
import com.xuannguyen.identityservice.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
