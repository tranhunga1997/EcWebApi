package com.example.useraccessdivide.product.repositories;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.useraccessdivide.product.entities.CartItemEntity;

@Repository
public interface CartRepository extends JpaRepository<CartItemEntity, Long> {
    Page<CartItemEntity> findByUserId(long userId, Pageable pageable);
    @Transactional @Modifying
    void deleteByUserIdAndProductId(long userId, long productId);
    @Transactional @Modifying
    void deleteByUserId(long userId);
}
