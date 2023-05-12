package com.user.user_management_system.user.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmailIgnoreCase(String email);

    Optional<User> findUserById(UUID id);
    List<User> findUsersByEmailOrPhoneNumber(String email, String phoneNumber);
    List<User> findAllByRoleAndStatusOrderByFirstNameAsc(UUID roleId, Boolean isEnabled);
    List<User> findAllByRoleAndStatusAndOfficeIdOrderByFirstNameAsc(UUID roleId, Boolean isEnabled, UUID officeId);
    Optional<User> findUserByEmailIgnoreCaseAndOtp(String email, String otp);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByOfficeId(UUID officeId, Pageable pageable);

}
