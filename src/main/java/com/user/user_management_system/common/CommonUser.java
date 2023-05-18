package com.user.user_management_system.common;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class CommonUser {
    private Boolean isActive;
    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updateAt;
}
