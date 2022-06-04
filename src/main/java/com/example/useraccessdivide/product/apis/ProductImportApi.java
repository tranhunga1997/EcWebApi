package com.example.useraccessdivide.product.apis;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product/import")
public class ProductImportApi {

	@PostMapping
	ResponseEntity importProduct() {
		
		return ResponseEntity.ok(null);
	}
}
