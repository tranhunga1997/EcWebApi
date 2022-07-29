package com.example.useraccessdivide.product.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.useraccessdivide.product.entities.CartItem;
import com.example.useraccessdivide.product.entities.CartItemPK;

@Repository
public interface CartRepository extends JpaRepository<CartItem, CartItemPK> {
    Page<CartItem> findByUserId(long userId, Pageable pageable);
    List<CartItem> findByUserId(long userId);
    
    @Transactional @Modifying
    void deleteByUserIdAndProductId(long userId, long productId);
    @Transactional @Modifying
    void deleteByUserId(long userId);
}
