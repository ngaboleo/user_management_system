package com.user.user_management_system.permission.service;
import com.user.user_management_system.Message.IMessageService;
import com.user.user_management_system.exception.HandleException;
import com.user.user_management_system.permission.dto.PermissionDto;
import com.user.user_management_system.permission.model.IPermissionRepository;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.user.model.User;
import com.user.user_management_system.util.PageObject;
import com.user.user_management_system.util.ResponseObject;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
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
        try {
            Optional<Permission> optionalPermission = iPermissionRepository.findPermissionById(id);
            Permission permission = optionalPermission.get();
            if (permission.getId() != id) {
                BeanUtils.copyProperties(permissionDto, permission);
                System.out.println(permission.getId());
                return new ResponseObject(iPermissionRepository.save(permission));
            }else {
                return new ResponseObject(IMessageService.PERMISSION_NOT_FOUND);
            }
        } catch (Exception exception) {
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject getAllPermission(Integer pageNumber, Integer pageSize) {
        try {
            Page<Permission> permissions = iPermissionRepository.findAll(PageObject.getPageable(pageNumber, pageSize));
            return new ResponseObject(permissions);
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }

    @Override
    public ResponseObject disablePermission(UUID id, PermissionDto permissionDto) {
        try {
            Optional<Permission> optionalPermission = iPermissionRepository.findPermissionById(id);
            Permission permission = optionalPermission.get();
            if (permission.getId() != id){
                permission.setIsActive(permissionDto.getIsActive());
                return new ResponseObject(iPermissionRepository.save(permission));
            }else {
                return new ResponseObject(IMessageService.PERMISSION_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new HandleException(exception);
        }
    }
}
