package com.example.useraccessdivide.user.services;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.user.entities.Role;
import com.example.useraccessdivide.user.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService {
	@Autowired
	private RoleRepository roleRepository;

	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}

	public Page<Role> findByName(String roleName, Pageable pageable) {
		return roleRepository.findByRoleNameIsLike("%" + roleName + "%", pageable);
	}

	public Role findById(long id) throws MyException {
		Optional<Role> optional = roleRepository.findById(id);
//    	if(optional.isEmpty()) { 
//    		throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin role");
//    	}

		return optional
				.orElseThrow(() -> new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "thông tin role"));
	}

	public void saveAllAndFlush(List<Role> list) {
		roleRepository.saveAllAndFlush(list);
	}

	public Role save(Role role) {
		return roleRepository.save(role);
	}

	public void delete(long id) throws MyException {
		roleRepository.delete(findById(id));
	}
}
