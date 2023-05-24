package com.user.user_management_system.role.service;

import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.permission.model.IPermissionRepository;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.role.dto.RoleDto;
import com.user.user_management_system.role.model.IRoleRepository;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.PageObject;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

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
                Optional<Permission> optionalPermission = iPermissionRepository.findPermissionById(permissionId);
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
            Optional<Role> optionalRole = iRoleRepository.findRoleById(role_id);
            Role role = optionalRole.get();
            if (optionalRole.isPresent()){
                for (UUID permissionId: roleDto.getPermissionsUuid()) {
                    Optional<Permission> optionalPermission = iPermissionRepository.findPermissionById(permissionId);
                    permissions.add(optionalPermission.get());
                }
                BeanUtils.copyProperties(roleDto, role);
                role.setIsActive(roleDto.getIsActive());
                role.setPermissions(permissions);
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

        try {
            Page<Role> roles = iRoleRepository.findAll(PageObject.getPageable(pageNumber, pageSize));
            return new ResponseObject(roles);
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject disableRole(UUID id, RoleDto roleDto) {
        try {
            Optional<Role> optionalRole = iRoleRepository.findRoleById(id);
            Role role = optionalRole.get();
            if (role.getId() != id){
                role.setIsActive(roleDto.getIsActive());
                return new ResponseObject(iRoleRepository.save(role));
            }else {
                return new ResponseObject(IMessageService.ROLE_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject roleSearch(String keyword) {
        try {
            List<Role> roleSearch = iRoleRepository.findByRoleNameContainingIgnoreCase(keyword);
            return new ResponseObject(roleSearch);
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }
}
