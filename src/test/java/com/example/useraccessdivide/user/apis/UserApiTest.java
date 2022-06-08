package com.example.useraccessdivide.user.apis;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import com.example.useraccessdivide.user.forms.RegisterForm;

@SpringBootTest
class UserApiTest {
	MockMvc mockMvc;
	
	@Autowired
	UserApi userApi;
	
	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.standaloneSetup(userApi).build();
	}

	/**
	 * Tạo 100000 user
	 * @throws Exception
	 */
	@Test
	void test() throws Exception {
		for(int i=2; i<=100000; i++) {
			mockMvc.perform(post("/api/user/register")
					.param("username", "test0000"+i)
					.param("password", "123456789")
					.param("matchPassword", "123456789")
					.param("email", "test0000"+i+"@gmail.com"))
			.andReturn();
		}
		
	}

	@Test
	void testViewRole() throws Exception {
		MvcResult result = mockMvc.perform(get("/api/admin/role").param("roleName", "admin")).andReturn();
	}
}
