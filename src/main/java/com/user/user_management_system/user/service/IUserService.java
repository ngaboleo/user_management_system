package com.user.user_management_system.user.service;

import com.user.user_management_system.user.dto.LoginRequest;
import com.user.user_management_system.user.dto.UpdatePasswordDto;
import com.user.user_management_system.user.dto.UserDto;
import com.user.user_management_system.util.ResponseObject;

import java.util.UUID;

public interface IUserService {
    ResponseObject createUser(UserDto userDto);
    ResponseObject login(LoginRequest loginRequest);
    ResponseObject updateUser(UUID userId, UserDto userDto);
    ResponseObject getAllUser(Integer pageNumber, Integer pageSize);
    ResponseObject findUserById(UUID id);
    ResponseObject findActiveUsersByRole(UUID roleId);
    ResponseObject updatePassword(UpdatePasswordDto updatePasswordDto);
    ResponseObject resetPassword(String email, String otp);
    ResponseObject sendOtp(String email);
    ResponseObject findAllUsersByOffice(UUID officeId, Integer pageNumber, Integer pageSize);
}
