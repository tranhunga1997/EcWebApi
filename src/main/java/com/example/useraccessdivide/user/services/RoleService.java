package com.example.useraccessdivide.user.services;

import com.example.useraccessdivide.user.entities.Role;
import com.example.useraccessdivide.user.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll(){
        return roleRepository.findAll(Sort.by("id"));
    }
    public Optional<Role> findById(long id){
        return roleRepository.findById(id);
    }
    public void saveAllAndFlush(List<Role> list){
        roleRepository.saveAllAndFlush(list);
    }
    public Role save(Role role){
        return roleRepository.save(role);
    }

    public void delete(long id){
        Optional<Role> roleOptional = findById(id);
        if(roleOptional.isPresent()){
            roleRepository.delete(roleOptional.get());
        }
    }
}
