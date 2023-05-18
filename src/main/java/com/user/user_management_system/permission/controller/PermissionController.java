package com.user.user_management_system.permission.controller;

import com.user.user_management_system.permission.dto.PermissionDto;
import com.user.user_management_system.permission.service.IPermissionService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService iPermissionService;
    @PostMapping("/create")
    public ResponseObject createPermission(@RequestBody PermissionDto permissionDto){
        try {
            return iPermissionService.createPermission(permissionDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
