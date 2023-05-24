package com.user.user_management_system.permission.controller;

import com.user.user_management_system.permission.dto.PermissionDto;
import com.user.user_management_system.permission.service.IPermissionService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @PutMapping("/update")
    public ResponseObject updatePermission(@RequestHeader UUID id, @RequestBody PermissionDto permissionDto){
        try {
            return iPermissionService.updatePermission(id, permissionDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/all")
    public ResponseObject getAllUser(@RequestHeader Integer pageNumber, @RequestHeader Integer pageSize){
        try {
            return iPermissionService.getAllPermission(pageNumber, pageSize);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @PutMapping("/disable/active")
    public ResponseObject disablePermission(@RequestHeader UUID id, @RequestBody PermissionDto permissionDto){
        try {
            return iPermissionService.disablePermission(id, permissionDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/search")
    public ResponseObject searchPermission(@RequestParam String keyword){
        try {
            return iPermissionService.searchPermission(keyword);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
