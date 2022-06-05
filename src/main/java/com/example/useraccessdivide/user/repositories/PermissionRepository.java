package com.example.useraccessdivide.user.repositories;

import com.example.useraccessdivide.user.entities.Permission;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	
	Page<Permission> findByPermissionNameIsLike(String name, Pageable pageable);
}
