package com.user.user_management_system.user.controller;

import com.user.user_management_system.user.dto.UserDto;
import com.user.user_management_system.user.service.IUserService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("user/create")
    public ResponseObject createUser(@RequestBody UserDto userDto){
        try {
            return userService.createUser(userDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
