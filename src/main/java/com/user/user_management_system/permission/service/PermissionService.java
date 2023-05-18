package com.user.user_management_system.permission.service;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.permission.dto.PermissionDto;
import com.user.user_management_system.permission.model.IPermissionRepository;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@Transactional
public class PermissionService implements IPermissionService{
    @Autowired
    private IPermissionRepository iPermissionRepository;
    @Override
    public ResponseObject createPermission(PermissionDto permissionDto) {
        try {
            Permission permission = new Permission();
            BeanUtils.copyProperties(permissionDto, permission);
            permission.setIsActive(true);
            return new ResponseObject(iPermissionRepository.save(permission));
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject updatePermission(UUID id, PermissionDto permissionDto) {
        return null;
    }

    @Override
    public ResponseObject getAllPermission(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
