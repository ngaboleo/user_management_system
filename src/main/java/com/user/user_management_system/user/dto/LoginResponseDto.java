package com.user.user_management_system.user.dto;

import com.user.user_management_system.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private User user;

    public LoginResponseDto(String token, User user) {
        this.token = "BEARER"+token;
        this.user = user;
    }
}
