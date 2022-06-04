package com.example.useraccessdivide.product.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.ProductImportEntity;
import com.example.useraccessdivide.product.forms.ProductImportForm;
import com.example.useraccessdivide.product.repositories.ProductImportRepository;

@Service
public class ProductImportService {
	@Autowired
	private ProductImportRepository productImportRepository;
	@Autowired
	private ProductService productService;
	
	public ProductImportEntity save(ProductImportForm form) {
		ProductImportEntity entity = new ProductImportEntity();
		entity.setProduct(productService.findById(form.getProductId()).get());
		entity.setQuantity(form.getQuantity());
		entity.setStoringDatetime(LocalDateTime.now());
		return productImportRepository.saveAndFlush(entity);
	}
	
	public List<ProductImportEntity> saveAll(List<ProductImportForm> forms){
		List<ProductImportEntity> entities = new ArrayList<ProductImportEntity>();
		forms.forEach(e -> {
			ProductImportEntity entity = new ProductImportEntity();
			entity.setProduct(productService.findById(e.getProductId()).get());
			entity.setQuantity(e.getQuantity());
			entity.setStoringDatetime(LocalDateTime.now());
			entities.add(entity);
		});
		return productImportRepository.saveAllAndFlush(entities);
	}
	
	
	public long getTotalQuantityProductOnYear(long productId, int year) {
		return productImportRepository.getTotalQuantityProductOnYear(productId, year);
	}
	
	public long getTotalQuantityProductOnYear(long productId, int year, int month) {
		return productImportRepository.getTotalQuantityProductOnYearMonth(productId, year, month);
	}
	
	public Page<ProductImportEntity> getAllProductImport(int year, Pageable pageable){
		return productImportRepository.getAllProductImport(year, pageable);
	}
	
	public Page<ProductImportEntity> getAllProductImport(int year, int month, Pageable pageable){
		return productImportRepository.getAllProductImport(year, month, pageable);
	}
	
	public Page<ProductImportEntity> getContainsProductName(String productName, Pageable pageable){
		return productImportRepository.findByProductNameLikeIgnoreCase(productName, pageable);
	}
}
