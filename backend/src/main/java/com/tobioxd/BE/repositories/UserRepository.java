package com.tobioxd.BE.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tobioxd.BE.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,String>{

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNunber);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByPasswordResetToken(String token);

    Optional<User> findByName(String name);

    boolean existsByPasswordResetToken(String passwordResetToken);

    @Query("SELECT o FROM User o WHERE (:keyword IS NULL OR :keyword = '' OR "+
            "LOWER(o.name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "+
            "LOWER(o.phoneNumber) LIKE LOWER(CONCAT('%',:keyword,'%'))) AND (LOWER(o.role) = 'user' OR LOWER(o.role) = 'receptionist')")
    Page<User> findAll(String keyword, Pageable pageable);

}
