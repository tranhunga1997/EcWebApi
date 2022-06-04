package com.example.useraccessdivide.product.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.product.repositories.CartHistoryRepository;
import com.example.useraccessdivide.product.repositories.ProductImportRepository;

@Service
public class ProductInvetoryService {
	@Autowired
	private CartHistoryRepository cartHistoryRepository;
	@Autowired
	private ProductImportRepository productImportRepository;
	
	

}
