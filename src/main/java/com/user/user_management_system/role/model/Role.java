package com.user.user_management_system.role.model;

import com.user.user_management_system.common.CommonUser;
import com.user.user_management_system.permission.model.Permission;
import com.user.user_management_system.user.model.User;
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
@Entity()
@Table(name = "roles")
public class Role extends CommonUser {
    @Id
    @GeneratedValue
    private UUID id;
    private String roleName;
    private String roleDescription;
    private Date roleValidityPeriod;
    @ManyToMany
    @JoinColumn(name = "permission_id")
    private Set<Permission> permissions = new HashSet<>();
}
