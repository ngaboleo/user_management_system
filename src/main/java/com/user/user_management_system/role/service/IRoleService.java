package com.user.user_management_system.role.service;

import com.user.user_management_system.role.dto.RoleDto;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.ResponseObject;

import java.util.UUID;

public interface IRoleService {
    ResponseObject createRole(RoleDto roleDto);
    ResponseObject updateRole(UUID role_id, RoleDto roleDto);
    ResponseObject getAllRoles(Integer pageNumber, Integer pageSize);
    ResponseObject disableRole(UUID id, RoleDto roleDto);

}
