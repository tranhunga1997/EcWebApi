package com.example.useraccessdivide.user.repositories;

import com.example.useraccessdivide.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.enable = ?2 WHERE u.id = ?1")
    void updateEnableUser(long id, boolean enable);
    
    @Modifying
    @Query("UPDATE User u SET u.email = :#{#user.email}, u.firstName = :#{#user.firstName}, u.lastName = :#{#user.lastName} " +
            "WHERE u.id = :#{#user.id}")
    void updateUser(User user);

}
