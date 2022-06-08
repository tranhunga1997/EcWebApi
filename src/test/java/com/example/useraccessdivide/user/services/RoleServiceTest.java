package com.example.useraccessdivide.user.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.useraccessdivide.user.entities.Permission;
import com.example.useraccessdivide.user.entities.Role;

@SpringBootTest
class RoleServiceTest {

	@Autowired
	private RoleService roleService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFindAll() {
		long expected = 2; // tổng số record data
		long actual = 0;
		Page<Role> rolePage = roleService.findAll(PageRequest.of(0, 5));
		actual = rolePage.getTotalElements();
		List<Role> list = rolePage.toList(); 
		Role role = list.get(0);
		List<Permission> pes= role.getPermissions();
		System.out.println(pes.get(0));
		assertEquals(expected, actual);
	}

	@Test
	void testFindByName() {
		String expected = "user";
		boolean actual = false;
		
		Page<Role> rolePage = roleService.findByName("%u%", PageRequest.of(0, 5));
		actual = rolePage.toList().stream().anyMatch(r -> expected.equals(r.getRoleName()));
		
		assertTrue(actual);
	}
	
	

}
