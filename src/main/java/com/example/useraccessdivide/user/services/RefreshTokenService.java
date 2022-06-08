package com.example.useraccessdivide.user.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.constant.CommonConstant;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.TokenProvider;
import com.example.useraccessdivide.user.entities.RefreshTokenEntity;
import com.example.useraccessdivide.user.repositories.RefreshTokenRepository;

@Service("tokenService")
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    
    /**
     * <dd>Giải thích: tạo mới refresh token (hạn 3 ngày)
     * @param userId
     * @param ipAddress
     * @return RefreshTokenEntity or null
     */
    public RefreshTokenEntity createToken(long userId, String ipAddress) {
    	RefreshTokenEntity newToken = new RefreshTokenEntity();
    	newToken.setRefreshToken(TokenProvider.generateRefreshToken());
    	newToken.setExpiryDatetime(CommonConstant.REFRESH_TOKEN_EXPYRIED_DAY);
    	newToken.setIpAddress(ipAddress);
    	newToken.setUserId(userId);
    	return refreshTokenRepository.saveAndFlush(newToken);
    }

    /**
     * <dd>Giải thích: tìm kiếm refresh token
     * @param rftoken
     * @return RefreshTokenEntity or null
     */
    public RefreshTokenEntity findByToken(String rftoken){
        return refreshTokenRepository.findByRefreshToken(rftoken);
    }
    
    public RefreshTokenEntity findById(long rfTokenId) throws MyException {
    	Optional<RefreshTokenEntity> optional = refreshTokenRepository.findById(rfTokenId);
    	if(optional.isEmpty()) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0003", "Refresh token");
    	}
    	return optional.get();
    }
    
    /**
     * <dd>Giải thích: kiểm tra tồn tại của token
     * @param rfToken
     * @return <code>true</code> tồn tại, <code>false</code> không tồn tại.
     */
    public boolean existsByRefreshToken(String rfToken){
        return refreshTokenRepository.existsByRefreshToken(rfToken);
    }
    
    /**
     * <dd>Giải thích: xóa refresh token
     * @param rftoken
     */
    public void deleteToken(String token){
        refreshTokenRepository.deleteRefreshTokenByRefreshToken(token);
    }
}
