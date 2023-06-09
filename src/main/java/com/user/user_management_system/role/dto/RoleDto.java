package com.user.user_management_system.role.dto;

import com.user.user_management_system.permission.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private String roleName;
    private String roleDescription;
    private Boolean isActive;
    private Date roleValidityPeriod;
    private Set<UUID> permissionsUuid;
}
