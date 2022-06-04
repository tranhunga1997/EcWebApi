package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.CategoryEntity;
import com.example.useraccessdivide.product.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySlug(String slug);

    List<ProductEntity> findByCategory(CategoryEntity category);
    //JOIN FETCH
}
