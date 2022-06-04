package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.BrandEntity;
import com.example.useraccessdivide.product.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findBySlug(String slug);
}
