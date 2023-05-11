package com.user.user_management_system.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordDto {
    private UUID userId;
    private String oldPassword;
    private String password;
}
