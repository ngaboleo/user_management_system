package com.user.user_management_system.role.controller;

import com.user.user_management_system.role.dto.RoleDto;
import com.user.user_management_system.role.service.IRoleService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;
    @PostMapping("/create")
    public ResponseObject createRole(@RequestBody RoleDto roleDto){
        try {
            return iRoleService.createRole(roleDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @PutMapping("/update")
    public ResponseObject updateRole(@RequestHeader UUID role_id, @RequestBody RoleDto roleDto){
        try {
            return iRoleService.updateRole(role_id, roleDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }

    @GetMapping("/all")
    public ResponseObject getAllRoles(@RequestHeader Integer pageNumber, @RequestHeader Integer pageSize){
        try {
            return iRoleService.getAllRoles(pageNumber, pageSize);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }

    @PutMapping("/disable")
    public ResponseObject disableRole(@RequestHeader UUID id, @RequestBody RoleDto roleDto){
        try {
            return iRoleService.disableRole(id, roleDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/search")
    public ResponseObject roleSearch(@RequestParam String keyword){
        try {
            return iRoleService.roleSearch(keyword);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
