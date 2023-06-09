package com.user.user_management_system.user.controller;

import com.user.user_management_system.user.dto.LoginRequest;
import com.user.user_management_system.user.dto.UserDto;
import com.user.user_management_system.user.service.IUserService;
import com.user.user_management_system.util.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    @PutMapping("/user/edit")
    public ResponseObject editUser(@RequestHeader UUID userId, @RequestBody UserDto userDto){
        try {
            return userService.editUser(userId, userDto);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/user/login")
    public ResponseObject login(@RequestBody LoginRequest loginRequest){
        try {
            return userService.login(loginRequest);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/user/activate")
    public ResponseObject activateAccount(@RequestBody LoginRequest loginRequest){
        try {
                return userService.activateAccount(loginRequest.getEmail(), loginRequest.getPassword());
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/user/all")
    public ResponseObject getAllUser(@RequestHeader Integer pageNUmber, @RequestHeader Integer pageSize){
        try {
            return userService.getAllUser(pageNUmber, pageSize);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/user/id")
    public ResponseObject findUserById(@RequestHeader UUID id){
        try {
            return userService.findUserById(id);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @GetMapping("/user/roleId")
    public ResponseObject findUsersROle(@RequestParam UUID id){
        try {
            return userService.findUsersByRole(id);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @PostMapping("/user/resetPassword")
    public ResponseObject resetPassword(@RequestHeader String email){
        try {
            return userService.resetPassword(email);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @PutMapping("/user/changePassword")
    public ResponseObject changePassword(@RequestHeader String token, @RequestHeader String newPassword){
        try {
            return userService.changePassword(token, newPassword);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }

    @GetMapping("/user/search")
    public ResponseObject userSearch(@RequestParam String keyword){
        try {
            return userService.userSearch(keyword);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
    @PutMapping("/user/sendOtp")
    public ResponseObject sendOtp(@RequestParam String email){
        try {
            return userService.sendOtp(email);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }

    @GetMapping("/user/verify/otp")
    public ResponseObject verifyOtp(@RequestHeader String email, @RequestHeader String otp){
        try {
            return userService.verifyOtp(email, otp);
        }catch (Exception exception){
            return new ResponseObject(exception);
        }
    }
}
