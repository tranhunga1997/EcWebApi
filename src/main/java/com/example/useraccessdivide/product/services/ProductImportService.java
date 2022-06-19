package com.example.useraccessdivide.product.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.product.entities.ProductImport;
import com.example.useraccessdivide.product.forms.ProductImportForm;
import com.example.useraccessdivide.product.repositories.ProductImportRepository;

@Service
public class ProductImportService {
	@Autowired
	private ProductImportRepository productImportRepository;
	@Autowired
	private ProductService productService;
	
	public ProductImport save(ProductImportForm form) throws MyException {
		ProductImport entity = new ProductImport();
		entity.setProduct(productService.findById(form.getProductId()));
		entity.setQuantity(form.getQuantity());
		entity.setStoringDatetime(LocalDateTime.now());
		return productImportRepository.saveAndFlush(entity);
	}
	
	public List<ProductImport> saveAll(List<ProductImportForm> forms){
		List<ProductImport> entities = new ArrayList<ProductImport>();
		forms.forEach(e -> {
			ProductImport entity = new ProductImport();
			try {
				entity.setProduct(productService.findById(e.getProductId()));
			} catch (MyException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
	
	public Page<ProductImport> getAllProductImport(int year, Pageable pageable){
		return productImportRepository.getAllProductImport(year, pageable);
	}
	
	public Page<ProductImport> getAllProductImport(int year, int month, Pageable pageable){
		return productImportRepository.getAllProductImport(year, month, pageable);
	}
	
	public Page<ProductImport> getContainsProductName(String productName, Pageable pageable){
		return productImportRepository.findByProductNameLikeIgnoreCase(productName, pageable);
	}
}
