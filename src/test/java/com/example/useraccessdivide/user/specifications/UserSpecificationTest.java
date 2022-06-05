package com.example.useraccessdivide.user.specifications;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.forms.UserForm;

@SpringBootTest
class UserSpecificationTest {

	@Autowired
	private UserSpecification specification;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testFilter() {
		String expected = "test001";
		String actual = "";
		UserForm form = new UserForm();
		form.setUsername("test");
		Page<User> page = specification.filter(form, PageRequest.of(0, 1));
		actual = page.toList().get(0).getUsername();
		
		assertEquals(expected, actual);
		
	}

}
