package com.bootakhae.webapp.wishlist.repositories;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.wishlist.entities.WishEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishEntity, Long> {
    Optional<WishEntity> findByProductAndUser(ProductEntity product, UserEntity user);
    Page<WishEntity> findAllByUser(UserEntity user, Pageable pageable);
    List<WishEntity> findAllByUser(UserEntity user);
}
