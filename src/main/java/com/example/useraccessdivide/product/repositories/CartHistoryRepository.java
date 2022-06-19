package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.CartHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartHistoryRepository extends JpaRepository<CartHistory, Long> {
    Page<CartHistory> findByUserId(long id, Pageable pageable);
}
