package com.user.user_management_system.permission.model;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface IPermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findPermissionById(UUID id);
    List<Permission> findByPermissionNameContainingIgnoreCase(String keyword);
}
