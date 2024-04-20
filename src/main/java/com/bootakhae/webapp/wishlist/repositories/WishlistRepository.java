package com.bootakhae.webapp.wishlist.repositories;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.wishlist.dto.response.ResponseWishDto;
import com.bootakhae.webapp.wishlist.entities.WishlistEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    Optional<WishlistEntity> findByProductAndUser(ProductEntity product, UserEntity user);
    Page<WishlistEntity> findAllByUser(UserEntity user, Pageable pageable);
}
