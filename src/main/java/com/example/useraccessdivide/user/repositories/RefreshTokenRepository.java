package com.example.useraccessdivide.user.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.useraccessdivide.user.entities.RefreshTokenEntity;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
	
	RefreshTokenEntity findByRefreshToken(String rfToken);
	
	boolean existsByRefreshToken(String rfToken);
	
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "UPDATE tokens SET token = :#{#token.token}, token_exp_date = :#{#token.tokenExpDate}, update_by = :#{#token.updateBy}, update_datetime = :#{#token.updateDatetime} WHERE id = :#{#token.id}",nativeQuery = true)
    int updateTokenByCreateBy(RefreshTokenEntity token);

//    @Transactional
    void deleteRefreshTokenByRefreshToken(String token);
    
    void deleteRefreshTokenById(long id);
}
