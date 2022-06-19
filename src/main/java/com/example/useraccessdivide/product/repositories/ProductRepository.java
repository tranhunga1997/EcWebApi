package com.example.useraccessdivide.product.repositories;

import com.example.useraccessdivide.product.entities.Category;
import com.example.useraccessdivide.product.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);

    List<Product> findByCategory(Category category);
    //JOIN FETCH
}
