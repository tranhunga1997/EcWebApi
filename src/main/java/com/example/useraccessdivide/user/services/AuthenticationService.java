package com.example.useraccessdivide.user.services;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.constant.CommonConstant;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.user.entities.AuthenticationsEntity;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.repositories.AuthenticationRepository;
import com.example.useraccessdivide.user.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;
/**
 * <dd>AUTHENTICATION SERVICE
 * @author Mạnh Hùng
 *
 */
@Service
@Slf4j
@Transactional
public class AuthenticationService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthenticationRepository authenticationRepository;
	
	/**
	 * <dd>Giải thích: tìm thông tin auth theo userId
	 * @param userId
	 * @return <code>AuthenticationsEntity</code>
	 */
	public AuthenticationsEntity findByUserId(long userId) {
		return authenticationRepository.findByUzer(userId);
	}
	
	/**
	 * <dd>Giải thích: lưu và thay đổi thông tin auth
	 * @param entity
	 */
	public void save(AuthenticationsEntity entity) {
		authenticationRepository.save(entity);
	}
	
	/**
	 * <dd>Giải thích: thay đổi mật khẩu
	 * @param entity
	 * @throws MyException 
	 */
	public void changePassword(long userId, String password) throws MyException {
		AuthenticationsEntity oldEntity = findByUserId(userId);
		AuthenticationsEntity newEntity = new AuthenticationsEntity();
		
		// xóa logic
		deleteLogic(userId);
		
		// thiết lập đối tượng newEntity
		newEntity.setHistoryId(oldEntity.getHistoryId()+1);
		newEntity.setAuthenticationCounter(0);
		newEntity.setCreateDatetime(LocalDateTime.now());
		newEntity.setPassword(password);
		newEntity.setUzer(oldEntity.getUzer());
		save(newEntity);
	}
	
	/**
	 * Xóa logic thông tin mật khẩu
	 * @param userId
	 * @return thông tin mật khẩu
	 * @throws MyException
	 */
	private AuthenticationsEntity deleteLogic(long userId) throws MyException {
		AuthenticationsEntity entity = findByUserId(userId);
		if(null == entity) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0004", "MSG_W0003", "Dữ liệu");
		}
		entity.setDeleteDatetime(LocalDateTime.now());
		save(entity);
		return entity;
	}
	
	/**
	 * <dd>Giải thích: xác thực tài khoản truy cập
	 * @param username
	 * @param password
	 * @return <code>true</code> thành công, <code>false</code> ngược lại
	 * @throws MyException 
	 */
	public void authentication(String username, String password) throws MyException {
		User user = loadUserByUsername(username);
		AuthenticationsEntity authEntity = findByUserId(user.getId());
		
		// kiểm tra mật khẩu
		if(!password.equals(authEntity.getPassword())) {
			authEntity.setAuthenticationCounter(authEntity.getAuthenticationCounter()+1);
			save(authEntity);
			throw new MyException(HttpStatus.BAD_REQUEST, "0003", "MSG_W0002", "Tài khoản hoặc mật khẩu");
		} else if (!user.isEnable()) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0005", "MSG_W0004");
		} else if (authEntity.getAuthenticationCounter() >= CommonConstant.LOGIN_FAILED_NUM) {
			userRepository.updateEnableUser(user.getId(), false);
			throw new MyException(HttpStatus.BAD_REQUEST, "0006", "MSG_W0005", CommonConstant.LOGIN_FAILED_NUM);
		}
	}
	
	/**
	 * <dd>Giải thích: kiểm tra tồn tại tài khoản
	 * @throws MyException 
	 */
	public User loadUserByUsername(String username) throws MyException{
        Optional<User> user = userRepository.findByUsername(username);
        if(!user.isPresent()){
            log.error("Tài khoản không tồn tại");
            throw new MyException(HttpStatus.BAD_REQUEST, "0002", "MSG_W0002", "Tài khoản hoặc mật khẩu");
        }
        return user.get();
	}
	
	

}
