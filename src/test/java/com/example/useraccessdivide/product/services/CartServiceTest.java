package com.example.useraccessdivide.product.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class CartServiceTest {
	@Autowired
	private CartService service;
	@Test
	void testFindByUserId1() {
		service.findByUserId(1, PageRequest.of(0, 5));
	}

}
