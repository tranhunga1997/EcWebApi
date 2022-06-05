package com.example.useraccessdivide.user.apis;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.constant.CommonConstant;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.CommonUtils;
import com.example.useraccessdivide.common.utils.TokenProvider;
import com.example.useraccessdivide.user.dtos.LoginResponse;
import com.example.useraccessdivide.user.entities.AuthenticationsEntity;
import com.example.useraccessdivide.user.entities.Permission;
import com.example.useraccessdivide.user.entities.RefreshTokenEntity;
import com.example.useraccessdivide.user.entities.Role;
import com.example.useraccessdivide.user.entities.User;
import com.example.useraccessdivide.user.forms.ChangePasswordForm;
import com.example.useraccessdivide.user.forms.ForgetPasswordForm;
import com.example.useraccessdivide.user.forms.LoginForm;
import com.example.useraccessdivide.user.forms.RegisterForm;
import com.example.useraccessdivide.user.forms.RoleForm;
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

	static final int PAGE_SIZE = 5;

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
	ResponseEntity<?> createUser(@Valid RegisterForm form) throws MyException {
		if (!form.getPassword().equals(form.getMatchPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "Xác nhận mật khẩu", "mật khẩu");
		}

		form.setUsername(form.getUsername().toLowerCase());
		boolean isExists = userService.isUserExists(form.getUsername());
		if (isExists) {
			return ResponseEntity.status(HttpStatus.FOUND).body("tài khoản đã tồn tại.");
		}
		Role role = roleService.findById(defaultRoleId);
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
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Lấy access token (JWT)")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "thất bại"),
			@ApiResponse(code = 200, message = "thành công") })
	@PostMapping("/user/get-jwt")
	ResponseEntity<String> generateJwtByRefreshToken(HttpServletRequest request) throws Exception {
		long refreshTokenId = TokenProvider.getRefreshTokenId(request);

		// Kiểm tra hạn sử dụng refresh token
		RefreshTokenEntity refreshTokenEntity = refreshTokenService.findById(refreshTokenId);
		if (refreshTokenEntity.getExpiryDatetime().isBefore(LocalDateTime.now())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "Refresh token");
		}
		// khởi tạo jwt
		User user = userService.findById(refreshTokenEntity.getUserId());
		String jwt = TokenProvider.generateJwt(user.getUsername(), refreshTokenEntity.getId());
		return ResponseEntity.status(HttpStatus.OK).body(jwt);
	}

	@ApiOperation(value = "Đăng nhập", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "thành công"),
			@ApiResponse(code = 400, message = "thất bại") })
	@PostMapping(value = "/user/login")
	ResponseEntity<LoginResponse> authenticateUser(HttpServletRequest request, @Valid LoginForm loginForm)
			throws MyException {
		User user = authService.loadUserByUsername(loginForm.getUsername());
		AuthenticationsEntity authEntity = authService.findByUserId(user.getId());

		// xác thực tài khoản
		authService.authentication(loginForm.getUsername(), loginForm.getPassword());

		String ipAddress = request.getRemoteAddr();
		// tạo refresh token
		long refTokenId = refreshTokenService.createToken(user.getId(), ipAddress).getId();

		// tạo JWT
		String jwt = TokenProvider.generateJwt(user.getUsername(), refTokenId);

		// thiết lập giá trị [đăng nhập thất bại] = 0
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);

		// in logger
		log.info(String.format("IP: %s, username: %s, login at: %s", ipAddress, user.getUsername(),
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));

		return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(jwt));
	}

	@ApiOperation(value = "Đăng xuất")
	@PostMapping("/user/logout")
	ResponseEntity<?> logout(HttpServletRequest request, @RequestBody String refreshToken) {
		refreshTokenService.deleteToken(refreshToken);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem thông tin chi tiết", authorizations = @Authorization(value = "thông tin khớp với username"))
	@ApiResponses(value = { @ApiResponse(code = 401, message = "") })
	@GetMapping("/user/detail/{username}")
	ResponseEntity<User> userDetail(HttpServletRequest request, @PathVariable String username) throws MyException {
		String authenticatedUsername = TokenProvider.getUsername(request);
		if (!username.equals(authenticatedUsername)) {
			throw new MyException(HttpStatus.NOT_FOUND, "9999", "MSG_W9999");
		}

		User user = authService.loadUserByUsername(username);

		return ResponseEntity.ok(user);
	}

	@ApiOperation(value = "Sửa thông tin chi tiết")
	@PutMapping("/user/detail/{username}")
	ResponseEntity<?> userDetailUpdate(HttpServletRequest request, @PathVariable String username,
			@RequestBody UserForm form) throws MyException {
		String authenticatedUsername = TokenProvider.getUsername(request);
		if (!username.equals(authenticatedUsername)) {
			throw new MyException(HttpStatus.NOT_FOUND, "9999", "MSG_W9999");
		}

		User user = authService.loadUserByUsername(username);
		user.setFirstName(form.getFirstName());
		user.setLastName(form.getLastName());
		user.setEmail(form.getEmail());
		userService.updateUser(user);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Quên mật khẩu")
	@PutMapping("/user/forget-password")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 404, message = "Tài khoản không tồn tại") })
	ResponseEntity<?> forgetPassword(@Valid ForgetPasswordForm form) throws MyException {
		if (!form.getPassword().equals(form.getMatchPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "Xác nhận mật khẩu mới", "mật khẩu mới");
		}

		User user = userService.findByEmail(form.getEmail());
		authService.changePassword(user.getId(), form.getPassword());
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Thay đổi mật khẩu")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 200, message = "Thành công") })
	@PutMapping("/user/change-password")
	ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordForm form) throws MyException {
		String username = TokenProvider.getUsername(request);
		User user = authService.loadUserByUsername(username);

		if (!form.getNewPwd().equals(form.getNewPwdConfirm())) {
			// kiểm tra mật khẩu mới khớp với mật khẩu mới (xác nhận)
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "Xác nhận mật khẩu mới", "mật khẩu mới");
		}

		if (form.getOldPwd().equals(form.getNewPwd())) {
			// mật khẩu mới không được giống mật khẩu cũ
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0006", "Mật khẩu mới", "mật khẩu cũ");
		}

		AuthenticationsEntity authEntity = authService.findByUserId(user.getId());
		if (!form.getOldPwd().equals(authEntity.getPassword())) {
			// kiểm tra mật khẩu cũ
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0002", "Mật khẩu cũ");
		}
		// ----- tiến hành thay đổi mật khẩu
		authEntity.setPassword(form.getNewPwd());
		authService.changePassword(authEntity.getUzer().getId(), form.getNewPwd());

		return ResponseEntity.ok().build();
	}

	/* TODO ADMIN PERMISSION */

	@ApiOperation(value = "Xem tất cả tài khoản")
	@GetMapping("/admin/list-user")
	ResponseEntity<Pagingation<User>> userList(HttpServletRequest request, @ApiParam(value = "null is find all") UserForm userForm,
			@RequestParam(defaultValue = "1") int page) throws MyException, Exception {
		checkPermission(CommonConstant.VIEW_USER_PER, TokenProvider.getUsername(request));
		
		Page<User> userPage = null;
		if (CommonUtils.isNull(userForm)) {
			userPage = userService.findAll(PageRequest.of(page - 1, PAGE_SIZE));
		} else {
			userPage = userSpecification.filter(userForm, PageRequest.of(page - 1, PAGE_SIZE));
		}
		return ResponseEntity
				.ok(new Pagingation<>(userPage.toList(), userPage.getTotalElements(), userPage.getTotalPages()));
	}

	@ApiOperation(value = "Mở và khóa tài khoản")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại"),
			@ApiResponse(code = 404, message = "Tài khoản không tồn tại") })
	@PostMapping("/admin/block-user")
	ResponseEntity<?> blockUser(HttpServletRequest request, @ApiParam(value = "id tài khoản", required = true) @RequestParam long id)
			throws Exception {
		checkPermission(CommonConstant.CHANGE_USER_BLOCK_STATUS_PER, TokenProvider.getUsername(request));
		
		AuthenticationsEntity authEntity = new AuthenticationsEntity();
		User user = userService.findById(id);

		// khóa hoặc mở khóa user
		userService.blockAndUnblock(user.getId(), !user.isEnable());

		// reset login failed counter về 0
		authEntity = authService.findByUserId(user.getId());
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem role chi tiết")
	@GetMapping("/admin/role")
	ResponseEntity<Pagingation<Role>> viewRole(HttpServletRequest request, String roleName, @RequestParam(defaultValue = "1") int page) throws MyException, Exception {
		checkPermission(CommonConstant.VIEW_ROLE_PER, TokenProvider.getUsername(request));
		
		int pageSize = 1;
		Page<Role> pageRole = null;

		if (CommonUtils.isNull(roleName)) {
			pageRole = roleService.findAll(PageRequest.of(page - 1, pageSize));
		} else {
			pageRole = roleService.findByName(roleName, PageRequest.of(page - 1, pageSize));
		}
		return ResponseEntity
				.ok(new Pagingation<>(pageRole.toList(), pageRole.getTotalElements(), pageRole.getTotalPages()));
	}

	@ApiOperation(value = "Thêm role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại") })
	@PostMapping("/admin/role")
	ResponseEntity<Role> createRole(HttpServletRequest request, RoleForm form) throws MyException, Exception {
		checkPermission(CommonConstant.CREATE_ROLE_PER, TokenProvider.getUsername(request));
		
		Role role = new Role();
		role.setRoleKey(CommonUtils.toSlug(form.getRoleName()));
		role.setRoleName(form.getRoleName());
		role.setCreateDatetime(LocalDateTime.now());
		Role roleResult = roleService.save(role);
		return ResponseEntity.ok(roleResult);
	}

	@ApiOperation(value = "Sửa tên role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Thất bại") })
	@PutMapping("/admin/role")
	ResponseEntity<Role> updateRole(HttpServletRequest request, @ApiParam(value = "id role", required = true) @RequestParam long id, RoleForm form)
			throws Exception {
		checkPermission(CommonConstant.UPDATE_ROLE_PER, TokenProvider.getUsername(request));
		
		Role role = roleService.findById(id);
		Set<Permission> permissions = permissionService.findByIds(form.getPermissionIds());
		role.setRoleKey(CommonUtils.toSlug(form.getRoleName()));
		role.setPermissions(permissions);
		role.setUpdateDatetime(LocalDateTime.now());

		Role roleResult = roleService.save(role);
		return ResponseEntity.ok(roleResult);
	}

	@ApiOperation(value = "Xóa role")
	@DeleteMapping("/admin/role")
	ResponseEntity<?> deleteRole(HttpServletRequest request, @ApiParam(value = "id role", required = true) @RequestParam long id) throws Exception {
		checkPermission(CommonConstant.DELETE_ROLE_PER, TokenProvider.getUsername(request));
		
		roleService.delete(id);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem permision")
	@GetMapping("/admin/permission")
	ResponseEntity<Pagingation<Permission>> viewPermission(HttpServletRequest request, String name, @RequestParam(defaultValue = "1") int page) throws MyException, Exception {
		checkPermission(CommonConstant.VIEW_PERMISSION_PER, TokenProvider.getUsername(request));
		
		Page<Permission> perPage = null;
		if(StringUtils.hasText(name)) {
			perPage = permissionService.findByName(name, PageRequest.of(page, PAGE_SIZE));
		} else {
			perPage = permissionService.findAll(PageRequest.of(page, PAGE_SIZE));
		}
		
		return ResponseEntity.ok(new Pagingation<>(perPage.toList(), perPage.getTotalElements(), perPage.getTotalPages()));
	}
	
	/**
	 * Kiểm tra permission
	 * @param permissionKey
	 * @param username
	 * @throws Exception tài khoản không có quyền sẽ ném lỗi.
	 */
	private void checkPermission(String permissionKey, String username) throws Exception {
		User user = userService.findByUsername(username);
		Set<Permission> permissions = user.getRole().getPermissions();
		
		boolean isMatch = permissions.stream().anyMatch(p -> permissionKey.equals(p.getPermissionKey()));
		if(!isMatch) {
			throw new Exception("Truy cập thất bại, ("+permissionKey+")");
		}
	}
}
