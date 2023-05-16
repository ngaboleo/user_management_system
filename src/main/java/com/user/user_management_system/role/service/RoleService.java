package com.user.user_management_system.role.service;

import com.user.user_management_system.permission.model.IPermissionRepository;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.role.dto.RoleDto;
import com.user.user_management_system.role.model.IRoleRepository;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository iRoleRepository;

    private Set<Permission> permissions;

    private Role role;


    @Override
    public ResponseObject createRole(RoleDto roleDto) {
        return null;
    }

    @Override
    public ResponseObject updateRole(UUID role_id, RoleDto roleDto) {
        return null;
    }

    @Override
    public ResponseObject getAllRoles(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
