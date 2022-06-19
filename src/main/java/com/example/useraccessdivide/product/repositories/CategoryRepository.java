package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.Brand;
import com.example.useraccessdivide.product.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findBySlug(String slug);
}
