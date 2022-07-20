package com.example.useraccessdivide.user.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.repositories.RoleRepository;
import com.example.useraccessdivide.user.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	public User findById(long id) throws MyException {
		Optional<User> optional = userRepository.findById(id);
//    	if(optional.isEmpty()) {
//    		throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản");
//    	}
		return optional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản"));
	}

	public User findByUsername(String username) throws MyException {
		Optional<User> optional = userRepository.findByUsername(username);
//		if (optional.isEmpty()) {
//			throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản");
//		}
		return optional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản"));
	}

	public Page<User> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public User saveAndFlush(User user) {
		user.setUpdateDatetime(LocalDateTime.now());
		user.setEnable(true);
		if (user.getRole() == null)
			user.setRole(roleRepository.findByRoleKey("user"));
		return userRepository.saveAndFlush(user);
	}

	public void updateUser(User user) {
		userRepository.updateUser(user);
	}

	public void saveAllAndFlush(List<User> userList) {
		userList.forEach(u -> {
			u.setUpdateDatetime(LocalDateTime.now());
		});
		userRepository.saveAllAndFlush(userList);
	}

	/**
	 * false: khóa, true: không khóa
	 * 
	 * @param id
	 * @param enable
	 */
	public void blockAndUnblock(long id, boolean isEnable) {
		userRepository.updateEnableUser(id, isEnable);
	}

	public boolean isUserExists(String username) {
		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			return true;
		}
		return false;
	}

	public User findByEmail(String email) throws MyException {
		Optional<User> userOptional = userRepository.findByEmail(email);
//		if (userOptional.isEmpty()) {
//			throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản");
//		}
		return userOptional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin tài khoản"));
	}
}
