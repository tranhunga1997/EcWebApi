package com.example.useraccessdivide.product.apis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class ProductApiTest {
	MockMvc mockMvc;
	@Autowired
	ProductApi productApi;
	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(productApi).build();
	}
	
	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/product")
				.param("name", "product 1")
				.param("price", "11234")
				.param("shortDescription", "mo ta ngan")
				.param("description", "mo ta dai")
				.param("categoryId", "5")
				.param("brandId", "4")
				.param("startDatetime", LocalDateTime.now().toString())
				).andReturn();
	}

}
