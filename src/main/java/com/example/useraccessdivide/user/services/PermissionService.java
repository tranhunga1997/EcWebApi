package com.example.useraccessdivide.user.services;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.user.entities.Permission;
import com.example.useraccessdivide.user.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Page<Permission> findAll(Pageable pageable){
        return permissionRepository.findAll(pageable);
    }
    
    public Page<Permission> findByName(String name, Pageable pageable) {
    	return permissionRepository.findByPermissionNameIsLike(name, pageable);
    }

    public Permission findById(long id) throws MyException{
    	Optional<Permission> optional = permissionRepository.findById(id);
    	if(optional.isEmpty()) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "Thông tin permission");
    	}
        return optional.get();
    }

    public Set<Permission> findByIds(List<Long> permissionIdList){
        return permissionRepository.findAllById(permissionIdList).stream().collect(Collectors.toSet());
    }

    public Permission save(Permission permission){
        return permissionRepository.save(permission);
    }

    public void saveAllAndFlush(List<Permission> list){
        permissionRepository.saveAllAndFlush(list);
    }

    public void delete(long id) throws MyException{
        permissionRepository.delete(findById(id));
    }
}
