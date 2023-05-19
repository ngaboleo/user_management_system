package com.user.user_management_system.permission.service;

import com.user.user_management_system.permission.dto.PermissionDto;
import com.user.user_management_system.util.ResponseObject;

import java.util.UUID;

public interface IPermissionService {
    ResponseObject createPermission(PermissionDto permissionDto);
    ResponseObject updatePermission(UUID id, PermissionDto permissionDto);
    ResponseObject getAllPermission(Integer pageNumber, Integer pageSize);
    ResponseObject disablePermission(UUID id, PermissionDto permissionDto);
}
