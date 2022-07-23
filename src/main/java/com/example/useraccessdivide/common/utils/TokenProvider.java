package com.example.useraccessdivide.common.utils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.example.useraccessdivide.common.exception.MyException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * Class chung liên quan các token, jwt
 * @author tranh
 *
 */
@Slf4j
public class TokenProvider {

    private static final String JWT_SECRET = "111111111111111111";
    private static final long JWT_EXPIRATION = 1*1*30*60L;	// ngày*giờ*phút*giây

    private TokenProvider() {}
    /**
     * Khởi tạo jwt 
     * @param userName tên tài khoản
     * @param refToken refresh token
     * @return jwt
     */
    public static String generateJwt(String userName, Long refTokenId){
        String jwt = Jwts.builder().setSubject(userName).setId(refTokenId.toString())
                .setIssuedAt(new Date()).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
        return jwt;
    }
    
    /**
     * Generate refresh token
     * @return refresh token
     */
    public static String generateRefreshToken() {
    	return UUID.randomUUID().toString();
    }
    
    /**
     * Lấy tên tài khoản từ request
     * @param request servlet request
     * @return username
     * @throws MyException 
     */
    public static String getUsername(HttpServletRequest request) throws MyException {
    	String jwt = getJwtFromRequest(request);
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }

    public static int getRefreshTokenId(HttpServletRequest request) throws MyException {
    	String jwt = getJwtFromRequest(request);
    	Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
    	return Integer.parseInt(claims.getId());
    }
    
    /**
     * Kiểm tra tính chính xác và hiệu lực jwt
     * @param jwt
     * @throws MyException 
     */
    private static void validateJwt(String jwt) throws MyException{
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
    	} catch (JwtException e) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "JWT");
    	}
    }

    /**
     * expiry date time: 1 day
     * @return
     */
    private static Date generateExpirationDate() {
        return Date.from(Instant.now().plusSeconds(JWT_EXPIRATION));
    }

    /**
     * Kiểm tra hiệu lực jwt
     * @param jwt
     * @return true hết hiệu lực, false còn hiệu lực 
     * @throws MyException 
     * validateJwt thay thế
     */
    @Deprecated
    private static boolean isTokenExpired(String jwt) throws MyException{
    	try {
	        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
	        return claims.getExpiration().after(new Date()) ? false : true;
    	} catch (Exception e) {
    		throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "JWT");
    	}
    }

    /**
     * Lấy jwt từ request
     * @param request servlet request
     * @return jwt
     * @throws MyException 
     */
    private static String getJwtFromRequest(HttpServletRequest request) throws MyException{
        String bearToken = request.getHeader("Authorization");
        if(!StringUtils.hasText(bearToken) || !bearToken.startsWith("Bearer ")){
            // kiểm tra token type (bearer)
        	throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "JWT");
        }
        
        bearToken = bearToken.replace("Bearer ", "");
        // kiểm tra jwt
        validateJwt(bearToken);
        return bearToken;
    }
}