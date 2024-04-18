package com.bootakhae.webapp.user.repositories;

import com.bootakhae.webapp.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
