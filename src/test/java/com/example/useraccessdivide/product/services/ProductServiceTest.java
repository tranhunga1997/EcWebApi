package com.example.useraccessdivide.product.services;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.example.useraccessdivide.common.exception.MyException;

@SpringBootTest
class ProductServiceTest {
	
	@Autowired
	ProductService service;
	
//	@Test
	void testGetByteImage() throws IOException {
		byte[] bs = service.getByteImage("coca-cola.jpg");
	}
	
	@Test
	void testAddProductWithCsv() throws IOException, MyException {
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\tranh\\Desktop\\product.csv"));
		MultipartFile multipartFile = new MockMultipartFile("product", "product.csv", null, fis.readAllBytes());
		service.saveProductWithCsv(multipartFile, true);
	}

}
