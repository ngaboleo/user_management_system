package com.user.user_management_system.role.service;

import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.permission.model.IPermissionRepository;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.role.dto.RoleDto;
import com.user.user_management_system.role.model.IRoleRepository;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class RoleService implements IRoleService {
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private IPermissionRepository iPermissionRepository;

    private Set<Permission> permissions = new HashSet<>();



    @Override
    public ResponseObject createRole(RoleDto roleDto) {
        // given permission id from dto
        // loop in those permission ids from dto then set permissionId to permission
        // add object of permission id to the set of permissions
        // set object permissions to a role
        try {
            for (UUID permissionId: roleDto.getPermissionsUuid()) {
                Optional<Permission> optionalPermission = iPermissionRepository.findById(permissionId);
                permissions.add(optionalPermission.get());
            }
            Role role = new Role();
            BeanUtils.copyProperties(roleDto, role);
            role.setPermissions(permissions);
            role.setIsActive(true);
            return new ResponseObject(iRoleRepository.save(role));
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject updateRole(UUID role_id, RoleDto roleDto) {
        try {
            Optional<Role> optionalRole = iRoleRepository.findById(role_id);
            if (optionalRole.isPresent()){
                Role role = new Role();
                BeanUtils.copyProperties(roleDto, role);
//                role.setRoleName(role.getRoleName());
//                role.setRoleDescription(roleDto.getRoleDescription());
//                role.setStatus(roleDto.getStatus());
//                role.setRoleValidityPeriod(roleDto.getRoleValidityPeriod());
//                role.setPermissions());
                return new ResponseObject(iRoleRepository.save(role));
            }else {
                throw new HandleException("role does not exist");
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject getAllRoles(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
