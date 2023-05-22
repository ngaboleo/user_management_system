package com.user.user_management_system.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IResetLinkerRepository extends JpaRepository<ResetLinker, UUID> {
    Optional<ResetLinker> findResetLinkerByTokenIgnoreCase(String token);
}
