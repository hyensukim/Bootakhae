package com.bootakhae.userservice.repositories;

import com.bootakhae.userservice.entities.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByUserId(String userId);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
    boolean existsByRefreshToken(String refreshToken);
}
