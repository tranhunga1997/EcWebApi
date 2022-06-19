package com.example.useraccessdivide.user.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.useraccessdivide.user.entities.Permission;

@SpringBootTest
class PermissionServiceTest {

	@Autowired
	PermissionService service;

	@Test
	void testSave() {
		Permission p = Permission.builder().permissionKey("test-1").permissionName("test 1")
				.createDatetime(LocalDateTime.now()).build();
		service.save(p);
	}

}
