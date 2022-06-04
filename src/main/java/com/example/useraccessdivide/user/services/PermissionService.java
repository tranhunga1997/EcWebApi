package com.example.useraccessdivide.user.services;

import com.example.useraccessdivide.user.entities.Permission;
import com.example.useraccessdivide.user.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public List<Permission> findAll(){
        return permissionRepository.findAll();
    }

    public Optional<Permission> findById(long id){
        return permissionRepository.findById(id);
    }

    public Set<Permission> findById(List<Long> permissionIdList){
        Set<Permission> permissionSet = new HashSet<>();
        permissionIdList.forEach(id -> {
            Optional<Permission> permissionOptional = findById(id);
            if(permissionOptional.isPresent()){
                permissionSet.add(permissionOptional.get());
            }
        });
        return permissionSet;
    }

    public Permission save(Permission permission){
        return permissionRepository.save(permission);
    }

    public void saveAllAndFlush(List<Permission> list){
        permissionRepository.saveAllAndFlush(list);
    }

    public void delete(long id){
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if(permissionOptional.isPresent()){
            permissionRepository.delete(permissionOptional.get());
        }
    }
}
