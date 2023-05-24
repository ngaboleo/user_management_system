package com.user.user_management_system.role.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IRoleRepository extends JpaRepository<Role, UUID> {
   Optional<Role> findRoleById(UUID id);
   List<Role> findByRoleNameContainingIgnoreCase(String keyword);
}
