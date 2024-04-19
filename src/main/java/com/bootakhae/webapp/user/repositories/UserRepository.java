package com.bootakhae.webapp.user.repositories;

import com.bootakhae.webapp.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(String userId);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
