package com.example.useraccessdivide.product.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.useraccessdivide.product.entities.ProductImportEntity;

@Repository
public interface ProductImportRepository extends JpaRepository<ProductImportEntity, Long> {
	@Query(value = "SELECT SUM(quantity) FROM product_imports WHERE product_id = :productId "
			+ "AND DATE_PART('year', storing_datetime) = :year " + "GROUP BY product_id",
			nativeQuery = true)
	long getTotalQuantityProductOnYear(long productId, int year);

	@Query(value = "SELECT SUM(quantity) FROM product_imports WHERE product_id = :productId "
			+ "AND DATE_PART('year', storing_datetime) = :year " + "AND DATE_PART('month',storing_datetime) = :month "
			+ "GROUP BY product_id",
			nativeQuery = true)
	long getTotalQuantityProductOnYearMonth(long productId, int year, int month);

	@Query(value = "SELECT * FROM product_imports WHERE DATE_PART('year', storing_datetime) = :year",
			countQuery = "SELECT COUNT(*) FROM product_imports WHERE DATE_PART('year', storing_datetime) = :year",
			nativeQuery = true)
	Page<ProductImportEntity> getAllProductImport(int year, Pageable pageable);

	@Query(value = "SELECT * FROM product_imports WHERE DATE_PART('year', storing_datetime) = :year "
			+ "AND DATE_PART('month', storing_datetime) = :month",
			countQuery = "SELECT COUNT(*) FROM product_imports WHERE DATE_PART('year', storing_datetime) = :year "
					+ "AND DATE_PART('month', storing_datetime) = :month",
			nativeQuery = true)
	Page<ProductImportEntity> getAllProductImport(int year, int month, Pageable pageable);
	
	
	Page<ProductImportEntity> findByProductNameLikeIgnoreCase(String productName, Pageable pageable);
}
