package com.example.useraccessdivide.user.apis;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.common.utils.TokenProvider;
import com.example.useraccessdivide.user.dtos.LoginResponse;
import com.example.useraccessdivide.user.dtos.ResponseData;
import com.example.useraccessdivide.user.dtos.UserDto;
import com.example.useraccessdivide.user.entities.AuthenticationsEntity;
import com.example.useraccessdivide.user.entities.Permission;
import com.example.useraccessdivide.user.entities.RefreshTokenEntity;
import com.example.useraccessdivide.user.entities.Role;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.forms.ChangePasswordForm;
import com.example.useraccessdivide.user.forms.ForgetPasswordForm;
import com.example.useraccessdivide.user.forms.LoginForm;
import com.example.useraccessdivide.user.forms.RegisterForm;
import com.example.useraccessdivide.user.forms.UserForm;
import com.example.useraccessdivide.user.services.AuthenticationService;
import com.example.useraccessdivide.user.services.PermissionService;
import com.example.useraccessdivide.user.services.RefreshTokenService;
import com.example.useraccessdivide.user.services.RoleService;
import com.example.useraccessdivide.user.services.UserService;
import com.example.useraccessdivide.user.specifications.UserSpecification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;

/**
 * <dd>API TÀI KHOẢN
 * 
 * @author Mạnh hùng
 *
 */

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@Api(value = "User Api", tags = { "api tài khoản" })
public class UserApi {

	@Autowired
	private UserSpecification userSpecification;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RefreshTokenService refreshTokenService;
	@Autowired
	private AuthenticationService authService;

	/**
	 * <dd>Giải thích: giới hạn số lượng đăng nhập
	 */
	@Value("${user.authentication.loginFailedCount}")
	private int loginFailedCount;
	/**
	 * <dd>Giải thích: role id mặc định khi tạo tài khoản.
	 */
	@Value("${user.default.roleId}")
	private long defaultRoleId;
	/* USER PERMISSION */

	@ApiOperation(value = "Đăng ký tài khoản", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 302, message = "Tài khoản đã tồn tại"),
			@ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping(value = "/user/register")
	ResponseEntity<String> createUser(@Valid RegisterForm form) {
		if (form == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		if (!form.getPassword().equals(form.getMatchPassword())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		form.setUsername(form.getUsername().toLowerCase());
		boolean isExists = userService.isUserExists(form.getUsername());
		if (isExists) {
			return ResponseEntity.status(HttpStatus.FOUND).body("tài khoản đã tồn tại.");
		}
		Role role = roleService.findById(defaultRoleId).get();
		User user = new User();
		user.setUsername(form.getUsername());
		user.setEmail(form.getEmail());
		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());
		user.setRole(role);
		user.setCreateDatetime(LocalDateTime.now());
		User authenticationUser = userService.saveAndFlush(user);

		AuthenticationsEntity entity = new AuthenticationsEntity();
		entity.setUzer(authenticationUser);
		entity.setPassword(form.getPassword());
		entity.setHistoryId(1);
		entity.setCreateDatetime(LocalDateTime.now());
		authService.save(entity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@ApiOperation(value = "Lấy access token (JWT)")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "thất bại"),
			@ApiResponse(code = 200, message = "thành công")
	})
	@PostMapping("/user/get-jwt")
	ResponseEntity<String> generateJwtByRefreshToken(HttpServletRequest request) throws Exception{
		long refreshTokenId = TokenProvider.getRefreshTokenId(request);
		
		// Kiểm tra hạn sử dụng refresh token
		RefreshTokenEntity refreshTokenEntity = refreshTokenService.findById(refreshTokenId);
		if(!refreshTokenEntity.getExpiryDatetime().isAfter(LocalDateTime.now())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "Refresh token");
		}
		// khởi tạo jwt
		User user = userService.findById(refreshTokenEntity.getUserId()).get();
		String jwt = TokenProvider.generateJwt(user.getUsername(), refreshTokenEntity.getId());
		return ResponseEntity.status(HttpStatus.OK).body(jwt);
	}

	@ApiOperation(value = "Đăng nhập", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "thành công"),
			@ApiResponse(code = 400, message = "thất bại") })
	@PostMapping(value = "/user/login")
	ResponseEntity<LoginResponse> authenticateUser(HttpServletRequest request,@Valid LoginForm loginForm) throws MyException {
		User user = null;
		AuthenticationsEntity authEntity = null;
		System.out.println("tai khoan: "+loginForm);
		// kiểm tra null 
		if (CommonUtils.isNull(loginForm)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		user = authService.loadUserByUsername(loginForm.getUsername());
		authEntity = authService.findByUserId(user.getId());

		// xác thực tài khoản
		authService.authentication(loginForm.getUsername(), loginForm.getPassword());

		long userId = user.getId();
		String ipAddress = request.getRemoteAddr();
		// tạo refresh token
		long refTokenId = refreshTokenService.createToken(userId, ipAddress).getId();
		
		// tạo JWT
		String jwt = TokenProvider.generateJwt(user.getUsername(), refTokenId);
		
		// thiết lập giá trị [đăng nhập thất bại] = 0
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);
		
		// in logger
		log.info(String.format("IP: %s, username: %s, login datetime: %s", ipAddress, user.getUsername(), LocalDateTime.now()));
		//
		return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(jwt, null));
	}
	
	/**
	 * <dd>Giải thích: đăng xuất
	 * @param authentication
	 * @return
	 */
	@ApiOperation(value = "Đăng xuất")
	@PostMapping("/user/logout")
	ResponseEntity logout(HttpServletRequest request, @RequestBody String refreshToken) {
//		UserCustomDetail userCustomDetail = (UserCustomDetail) authService.loadUserByUsername(authentication.getName());
		//String token = refreshTokenService.findByCreateBy(userCustomDetail.getUser().getId()).getToken();
		//refreshTokenService.deleteTokenByTokenAndCreateBy(token, userCustomDetail.getUser().getId());
		refreshTokenService.deleteToken(refreshToken);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	/**
	 * <dd>Giải thích: xem thông tin chi tiết
	 * @param username
	 * @param authentication
	 * @return
	 * @throws DataHiddenException 
	 * @throws MyException 
	 */
	@ApiOperation(value = "Xem thông tin chi tiết", authorizations = @Authorization(value = "thông tin khớp với username"))
	@ApiResponses(value = { @ApiResponse(code = 401, message = "") })
	@GetMapping("/user/detail/{username}")
	ResponseEntity<ResponseData<UserDto>> userDetail(HttpServletRequest request, @PathVariable String username) throws MyException {
		ResponseData<UserDto> responseData = new ResponseData<UserDto>();
		String authenticatedUsername = TokenProvider.getUsername(request);
		if(!username.equals(authenticatedUsername)) {
			throw new MyException(HttpStatus.NOT_FOUND, "9999", "MSG_W9999");
		}
		
		User user = authService.loadUserByUsername(username);
		UserDto userDto = UserDto.builder()
				.id(user.getId())
				.username(user.getUsername())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail()).enable(user.isEnable()).build();
		responseData.setCode("200");
		responseData.setMessage("Thành công.");
		responseData.setObj(userDto);
		return ResponseEntity.ok(responseData);
	}

	/**
	 * <dd>Giải thích: sửa thông tin chi tiết
	 * @param username
	 * @param form
	 * @param authentication
	 * @return
	 * @throws MyException 
	 */
	@ApiOperation(value = "Sửa thông tin chi tiết")
	@PutMapping("/user/detail/{username}")
	ResponseEntity<String> userDetailUpdate(HttpServletRequest request, @PathVariable String username, @RequestBody UserForm form) throws MyException {
		String authenticatedUsername = TokenProvider.getUsername(request);
		if(!username.equals(authenticatedUsername)) {
			throw new MyException(HttpStatus.NOT_FOUND, "9999", "MSG_W9999");
		}
		
		User user = authService.loadUserByUsername(username);
		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());
		user.setEmail(form.getEmail());
		userService.updateUser(user);
		return ResponseEntity.ok("Sửa thông tin tài khoản thành công.");
	}

	/**
	 * <dd>Giải thích: quên mật khẩu
	 * @param form
	 * @return
	 * @throws NotSameObjectException 
	 * @throws MyException 
	 */
	@ApiOperation(value = "Quên mật khẩu")
	@PutMapping("/user/forget-password")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 404, message = "Tài khoản không tồn tại") })
	ResponseEntity<ResponseData> forgetPassword(@Valid ForgetPasswordForm form) throws MyException {
		ResponseData<?> responseData = new ResponseData();
		
		if (CommonUtils.isNull(form.getMatchPassword(), form.getPassword(), form.getEmail())) {
			throw new NullPointerException("input is null");
		}

		if (!form.getPassword().equals(form.getMatchPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "Xác nhận mật khẩu mới", "mật khẩu mới");
		}

		User user = userService.findByEmail(form.getEmail());
		if (user == null) {
			throw new NullPointerException("Email không chính xác");
		}

		authService.changePassword(user.getId(), form.getPassword());
		
		responseData.setCode("200");
		responseData.setMessage("Thay đổi mật khẩu thành công.");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * <dd>Giải thích: thay đổi mật khẩu
	 * @param form
	 * @return
	 * @throws MyException 
	 * @throws NotSameObjectException
	 */
	@ApiOperation(value = "Thay đổi mật khẩu")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 200, message = "Thành công") })
	@PutMapping("/user/change-password")
	ResponseEntity<ResponseData> changePassword(HttpServletRequest request, ChangePasswordForm form) throws MyException {
		String username = TokenProvider.getUsername(request);
		
		// khởi tạo đối tượng
		ResponseData responseData = new ResponseData();
		User user = authService.loadUserByUsername(username);
		
		// kiểm tra mật khẩu mới khớp với mật khẩu mới (xác nhận)
		if (!form.getNewPwd().equals(form.getNewPwdConfirm())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "Xác nhận mật khẩu mới", "mật khẩu mới");
		}
		
		// mật khẩu mới không được giống mật khẩu cũ
		if(form.getOldPwd().equals(form.getNewPwd())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0006", "Mật khẩu mới", "mật khẩu cũ");
		}
		
		AuthenticationsEntity authEntity = authService.findByUserId(user.getId());
		// kiểm tra mật khẩu cũ
		if(!form.getOldPwd().equals(authEntity.getPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0002", "Mật khẩu cũ");
		}
		// ----- tiến hành thay đổi mật khẩu
		authEntity.setPassword(form.getNewPwd());
		authService.changePassword(authEntity.getUzer().getId(), form.getNewPwd());
		
		// thiết lập response
		responseData.setCode("200");
		responseData.setMessage("Đổi mật khẩu thành công.");
		return ResponseEntity.status(HttpStatus.OK).body(responseData);
	}

	/* ADMIN PERMISSION */

	/**
	 * <dd>Giải thích: xem tất cả thông tin user (admin)
	 * 
	 * @return
	 */
	@ApiOperation(value = "Xem tất cả tài khoản")
	@GetMapping("/admin/list-user")
	ResponseEntity<List<UserDto>> userList(@ApiParam(value = "null is find all") UserForm userForm) {
		if (userForm == null) {
			return ResponseEntity.ok(userSpecification.findAll());
		}
		List<UserDto> userDtoList = userSpecification.filter(userForm.convertToUserDto());
		return ResponseEntity.ok(userDtoList);
	}
	
	/**
	 * <dd>Giải thích: mở và khóa tài khoản
	 * @param id
	 * @return
	 * @throws NullPointerException
	 */
	@ApiOperation(value = "Mở và khóa tài khoản")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 404, message = "Tài khoản không tồn tại") })
	@PostMapping("/admin/block-user")
	ResponseEntity<ResponseData> blockUser(@ApiParam(value = "id tài khoản", required = true) @RequestParam long id) throws NullPointerException {
		ResponseData responseData = new ResponseData();
		User user = new User();
		AuthenticationsEntity authEntity = new AuthenticationsEntity();
		Optional<User> userOptional = userService.findById(id);
		
		if (!userOptional.isPresent()) {
			throw new NullPointerException("Tài khoản không tồn tại");
		}
		
		// khóa hoặc mở khóa user
		user = userOptional.get();
		userService.blockAndUnblock(user.getId(), !user.isEnable());
		
		// reset login failed counter về 0
		authEntity = authService.findByUserId(user.getId());
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);
		
		// thiết lập response data
		responseData.setCode("200");
		responseData.setMessage("Thành công");
		return ResponseEntity.status(200).body(responseData);
	}

	/**
	 * <dd>Giải thích: xem role
	 * @return
	 */
	@ApiOperation(value = "Xem role chi tiết")
	@GetMapping("/admin/role")
	ResponseEntity<List<Role>> viewRole() {
		return ResponseEntity.ok(roleService.findAll());
	}

	/**
	 * <dd>Giải thích: thêm role
	 * @param roleName
	 * @return
	 */
	@ApiOperation(value = "Thêm role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/admin/role")
	ResponseEntity<Role> createRole(@RequestParam String roleName) {
		Role role = new Role();
		role.setRoleKey(CommonUtils.toSlug(roleName));
		role.setRoleName(roleName);
		role.setCreateDatetime(LocalDateTime.now());
		role.setUpdateDatetime(LocalDateTime.now());
		Role roleResult = roleService.save(role);
		return ResponseEntity.ok(roleResult);
	}

	/**
	 * <dd>Giải thích: sửa role
	 * @param id
	 * @param roleName
	 * @return
	 */
	@ApiOperation(value = "Sửa tên role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/admin/role")
	ResponseEntity<Role> updateRole(@ApiParam(value = "id role", required = true) @RequestParam long id,
			@RequestParam String roleName) {
		Optional<Role> roleOptional = roleService.findById(id);
		if (roleOptional.isPresent()) {
			Role role = new Role();
			role.setId(roleOptional.get().getId());
			role.setRoleKey(CommonUtils.toSlug(roleName));
			role.setRoleName(roleName);
			role.setPermissions(roleOptional.get().getPermissions());
			role.setCreateDatetime(roleOptional.get().getCreateDatetime());
			role.setUpdateDatetime(LocalDateTime.now());
			Role roleResult = roleService.save(role);
			return ResponseEntity.ok(roleResult);
		}
		return ResponseEntity.status(400).build();
	}

	/**
	 * <dd>Giải thích: xóa role
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "Xóa role")
	@DeleteMapping("/admin/role")
	ResponseEntity deleteRole(@ApiParam(value = "id role", required = true) @RequestParam long id) {
		roleService.delete(id);
		return ResponseEntity.status(200).build();
	}

	/**
	 * <dd>Giải thích: sửa permission của role
	 * @param roleId
	 * @param permissionIds
	 * @return
	 */
	@ApiOperation(value = "Thêm permission vào role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/admin/role/permission")
	ResponseEntity<Role> updatePermissionInRole(@RequestParam long roleId, @RequestParam List<Long> permissionIds) {
		Set<Permission> permissionSet = permissionService.findById(permissionIds);
		Role role = roleService.findById(roleId).get();
		role.setPermissions(permissionSet);
		Role roleResult = roleService.save(role);
		return ResponseEntity.ok(roleResult);
	}

	/**
	 * <dd>Giải thích: xem tất cả permission
	 * @return
	 */
	@ApiOperation(value = "Xem permision")
	@GetMapping("/admin/permission")
	ResponseEntity<List<Permission>> viewPermission() {
		return ResponseEntity.ok(permissionService.findAll());
	}
}
