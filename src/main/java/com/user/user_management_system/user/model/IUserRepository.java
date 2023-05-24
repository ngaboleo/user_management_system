package com.user.user_management_system.user.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmailIgnoreCase(String email);

    Optional<User> findUserById(UUID id);
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.phoneNumber = :phoneNumber")
    List<User> findUsersByEmailOrPhoneNumber(@Param("email") String email, @Param("phoneNumber") String phoneNumber);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.id= :roleId")
    List<User> findUsersByRolesId(@Param("roleId") UUID roleId);
    Page<User> findAll(Pageable pageable);
    List<User> findByFullNameContainingIgnoreCase(String keyword);


}
