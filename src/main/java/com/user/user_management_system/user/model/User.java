package com.user.user_management_system.user.model;

import com.user.user_management_system.common.CommonUser;
import com.user.user_management_system.office.model.Office;
import com.user.user_management_system.role.model.Role;
import com.user.user_management_system.util.OtpChannel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends CommonUser {
    @Id
    @GeneratedValue
    private UUID id;
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
    private String otp;
    private String documentId;
    private String documentType;
    private String jurisdictionLevel;
    private Boolean isEnabled;
    @Enumerated(EnumType.STRING)
    private OtpChannel otpChannel;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Set<Role> roles = new HashSet<>();
    @ManyToOne
    @JoinColumn(name = "office_id")
    private Office office;

}

