package com.example.useraccessdivide.product.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {
	
	@Autowired
	ProductService service;
	
	@Test
	void testGetByteImage() throws IOException {
		byte[] bs = service.getByteImage("coca-cola.jpg");
	}

}
