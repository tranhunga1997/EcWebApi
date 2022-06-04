package com.example.useraccessdivide.user.repositories;

import com.example.useraccessdivide.user.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRoleKey(String roleKey);
}
