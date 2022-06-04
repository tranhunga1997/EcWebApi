package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.CartHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartHistoryRepository extends JpaRepository<CartHistoryEntity, Long> {
    Page<CartHistoryEntity> findByUserId(long id, Pageable pageable);
}
