package com.user.user_management_system.permission.model;

import com.user.user_management_system.common.CommonUser;
import com.user.user_management_system.role.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends CommonUser {
    @Id
    @GeneratedValue
    private UUID id;
    private String permissionName;

}
