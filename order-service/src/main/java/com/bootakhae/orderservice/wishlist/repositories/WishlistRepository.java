package com.bootakhae.orderservice.wishlist.repositories;

import com.bootakhae.orderservice.wishlist.entities.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

//public interface WishlistRepository extends CrudRepository<Wishlist, String> {
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findAllByUserId(String userId);
    Page<Wishlist> findAllByUserId(String userId, Pageable pageable);
    Optional<Wishlist> findByUserIdAndProductId(String userId, String productId);
}
