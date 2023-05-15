package com.user.user_management_system.office.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface IOfficeRepository extends JpaRepository<Office, UUID> {
    Office findOfficeByOfficeName(String officeName);

    Optional<Office> findById(UUID uuid);
}
