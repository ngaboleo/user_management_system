package com.user.user_management_system.user.dto;

import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.OtpChannel;
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
public class UserDto {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String otp;
    private String documentId;
    private String documentType;
    private String jurisdictionLevel;
    private Boolean isEnabled;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private OtpChannel otpChannel;
    private UUID officeId;
    private Set<UUID> roles;
}
