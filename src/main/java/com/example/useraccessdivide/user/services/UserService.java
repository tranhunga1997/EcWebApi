package com.example.useraccessdivide.user.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Optional<User> findById(long id){
        return userRepository.findById(id);
    }

//    public void saveAndFlush(User user){
//        user.setUpdateDatetime(LocalDateTime.now());
//        user.setEnable(true);
//        if(user.getRole() == null)
//            user.setRole(roleRepository.findByRoleKey("user"));
//        userRepository.saveAndFlush(user);
//    }
    
    public User saveAndFlush(User user){
        user.setUpdateDatetime(LocalDateTime.now());
        user.setEnable(true);
        if(user.getRole() == null)
            user.setRole(roleRepository.findByRoleKey("user"));
        return userRepository.saveAndFlush(user);
    }

    public void updateUser(User user){
        userRepository.updateUser(user);
    }

    public void saveAllAndFlush(List<User> userList){
        userList.forEach(u ->{
            u.setUpdateDatetime(LocalDateTime.now());
        });
        userRepository.saveAllAndFlush(userList);
    }
    /**
     * false: khóa, true: không khóa 
     * @param id
     * @param enable
     */
    public void blockAndUnblock(long id, boolean isEnable){
        userRepository.updateEnableUser(id,isEnable);
    }

    public boolean isUserExists(String username){
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            return true;
        }
        return false;
    }

    public User findByEmail(String email){
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            return null;
        }
        return userOptional.get();
    }
}
