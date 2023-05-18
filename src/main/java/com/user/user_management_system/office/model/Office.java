package com.user.user_management_system.office.model;

import com.user.user_management_system.common.CommonUser;
import com.user.user_management_system.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offices")
public class Office extends CommonUser {
    @Id
    @GeneratedValue
    private UUID id;
    private String officeName;
    private String officeLocation;

}
