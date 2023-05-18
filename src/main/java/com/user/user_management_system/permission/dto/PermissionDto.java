package com.user.user_management_system.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private String permissionName;
    private Boolean status;
}
