package com.bootakhae.webapp.wishlist.repositories;

import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.user.entities.UserEntity;
import com.bootakhae.webapp.wishlist.entities.WishListEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishListEntity, Long> {
    Optional<WishListEntity> findByProductAndUser(ProductEntity product, UserEntity user);
}
