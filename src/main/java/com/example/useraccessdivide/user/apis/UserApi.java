package com.example.useraccessdivide.user.apis;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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
import com.example.useraccessdivide.user.dtos.RoleDetailDto;
import com.example.useraccessdivide.user.dtos.RoleDto;
import com.example.useraccessdivide.user.dtos.UserDetailDto;
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
 * <dd>API T??I KHO???N
 * 
 * @author M???nh h??ng
 *
 */

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping("/api")
@Api(value = "User Api", tags = { "api t??i kho???n" })
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
	 * <dd>Gi???i th??ch: role id m???c ?????nh khi t???o t??i kho???n.
	 */
	@Value("${user.default.roleId}")
	private long defaultRoleId;
	/* USER PERMISSION */

	@ApiOperation(value = "????ng k?? t??i kho???n", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 302, message = "T??i kho???n ???? t???n t???i"),
			@ApiResponse(code = 400, message = "Th???t b???i") })
	@PostMapping(value = "/user/register")
	ResponseEntity<?> createUser(@Valid RegisterForm form) throws MyException {
		if (!form.getPassword().equals(form.getMatchPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "X??c nh???n m???t kh???u", "m???t kh???u");
		}

		form.setUsername(form.getUsername().toLowerCase());
		boolean isExists = userService.isUserExists(form.getUsername());
		if (isExists) {
			return ResponseEntity.status(HttpStatus.FOUND).body("t??i kho???n ???? t???n t???i.");
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

	@ApiOperation(value = "L???y access token (JWT)")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "th???t b???i"),
			@ApiResponse(code = 200, message = "th??nh c??ng") })
	@PostMapping("/user/get-jwt")
	ResponseEntity<String> generateJwtByRefreshToken(HttpServletRequest request) throws Exception {
		long refreshTokenId = TokenProvider.getRefreshTokenId(request);

		// Ki???m tra h???n s??? d???ng refresh token
		RefreshTokenEntity refreshTokenEntity = refreshTokenService.findById(refreshTokenId);
		if (refreshTokenEntity.getExpiryDatetime().isBefore(LocalDateTime.now())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0001", "MSG_W0001", "Refresh token");
		}
		// kh???i t???o jwt
		User user = userService.findById(refreshTokenEntity.getUserId());
		String jwt = TokenProvider.generateJwt(user.getUsername(), refreshTokenEntity.getId());
		return ResponseEntity.status(HttpStatus.OK).body(jwt);
	}

	@ApiOperation(value = "????ng nh???p", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "th??nh c??ng"),
			@ApiResponse(code = 400, message = "th???t b???i") })
	@PostMapping(value = "/user/login")
	ResponseEntity<LoginResponse> authenticateUser(HttpServletRequest request, @Valid LoginForm loginForm)
			throws MyException {
		User user = authService.loadUserByUsername(loginForm.getUsername());
		AuthenticationsEntity authEntity = authService.findByUserId(user.getId());

		// x??c th???c t??i kho???n
		authService.authentication(loginForm.getUsername(), loginForm.getPassword());

		String ipAddress = request.getRemoteAddr();
		// t???o refresh token
		long refTokenId = refreshTokenService.createToken(user.getId(), ipAddress).getId();

		// t???o JWT
		String jwt = TokenProvider.generateJwt(user.getUsername(), refTokenId);

		// thi???t l???p gi?? tr??? [????ng nh???p th???t b???i] = 0
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);

		// in logger
		log.info(String.format("IP: %s, username: %s, login at: %s", ipAddress, user.getUsername(),
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));

		return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(jwt));
	}

	@ApiOperation(value = "????ng xu???t")
	@PostMapping("/user/logout")
	ResponseEntity<?> logout(HttpServletRequest request, @RequestBody String refreshToken) {
		refreshTokenService.deleteToken(refreshToken);
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Xem th??ng tin chi ti???t", authorizations = @Authorization(value = "th??ng tin kh???p v???i username"))
	@ApiResponses(value = { @ApiResponse(code = 401, message = "") })
	@GetMapping("/user/detail/{username}")
	ResponseEntity<UserDetailDto> userDetail(HttpServletRequest request, @PathVariable String username) throws MyException {
		String authenticatedUsername = TokenProvider.getUsername(request);
		if (!username.equals(authenticatedUsername)) {
			throw new MyException(HttpStatus.NOT_FOUND, "9999", "MSG_W9999");
		}
		UserDetailDto userDetailDto = new UserDetailDto();
		User user = authService.loadUserByUsername(username);
		BeanUtils.copyProperties(user, userDetailDto);

		return ResponseEntity.ok(userDetailDto);
	}

	@ApiOperation(value = "S???a th??ng tin chi ti???t")
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

	@ApiOperation(value = "Qu??n m???t kh???u")
	@PutMapping("/user/forget-password")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Th???t b???i"),
			@ApiResponse(code = 404, message = "T??i kho???n kh??ng t???n t???i") })
	ResponseEntity<?> forgetPassword(@Valid ForgetPasswordForm form) throws MyException {
		if (!form.getPassword().equals(form.getMatchPassword())) {
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "X??c nh???n m???t kh???u m???i", "m???t kh???u m???i");
		}

		User user = userService.findByEmail(form.getEmail());
		authService.changePassword(user.getId(), form.getPassword());
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Thay ?????i m???t kh???u")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Th???t b???i"),
			@ApiResponse(code = 200, message = "Th??nh c??ng") })
	@PutMapping("/user/change-password")
	ResponseEntity<?> changePassword(HttpServletRequest request, ChangePasswordForm form) throws MyException {
		String username = TokenProvider.getUsername(request);
		User user = authService.loadUserByUsername(username);

		if (!form.getNewPwd().equals(form.getNewPwdConfirm())) {
			// ki???m tra m???t kh???u m???i kh???p v???i m???t kh???u m???i (x??c nh???n)
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0007", "X??c nh???n m???t kh???u m???i", "m???t kh???u m???i");
		}

		if (form.getOldPwd().equals(form.getNewPwd())) {
			// m???t kh???u m???i kh??ng ???????c gi???ng m???t kh???u c??
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0006", "M???t kh???u m???i", "m???t kh???u c??");
		}

		AuthenticationsEntity authEntity = authService.findByUserId(user.getId());
		if (!form.getOldPwd().equals(authEntity.getPassword())) {
			// ki???m tra m???t kh???u c??
			throw new MyException(HttpStatus.BAD_REQUEST, "0007", "MSG_W0002", "M???t kh???u c??");
		}
		// ----- ti???n h??nh thay ?????i m???t kh???u
		authEntity.setPassword(form.getNewPwd());
		authService.changePassword(authEntity.getUzer().getId(), form.getNewPwd());

		return ResponseEntity.ok().build();
	}

	/* TODO ADMIN PERMISSION */

	@ApiOperation(value = "Xem t???t c??? t??i kho???n")
	@GetMapping("/admin/list-user")
	ResponseEntity<Pagingation<UserDto>> userList(HttpServletRequest request, @ApiParam(value = "null is find all") UserForm userForm,
			@RequestParam(defaultValue = "1") int page) throws MyException, Exception {
		checkPermission(CommonConstant.VIEW_USER_PER, TokenProvider.getUsername(request));
		
		Page<User> userPage = userSpecification.filter(userForm, PageRequest.of(page - 1, CommonConstant.PAGE_SIZE));
		List<UserDto> userDtos = userPage.stream().map(u -> {
			UserDto dto = new UserDto();
			dto.setId(u.getId());
			dto.setUsername(u.getUsername());
			dto.setFirstName(u.getFirstName());
			dto.setLastName(u.getLastName());
			return dto;
		}).collect(Collectors.toList());
		return ResponseEntity
				.ok(new Pagingation<>(userDtos, userPage.getTotalElements(), userPage.getTotalPages()));
	}

	@ApiOperation(value = "M??? v?? kh??a t??i kho???n")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Th???t b???i"),
			@ApiResponse(code = 404, message = "T??i kho???n kh??ng t???n t???i") })
	@PostMapping("/admin/block-user")
	ResponseEntity<?> blockUser(HttpServletRequest request, @ApiParam(value = "id t??i kho???n", required = true) @RequestParam long id)
			throws Exception {
		checkPermission(CommonConstant.CHANGE_USER_BLOCK_STATUS_PER, TokenProvider.getUsername(request));
		
		AuthenticationsEntity authEntity = new AuthenticationsEntity();
		User user = userService.findById(id);

		// kh??a ho???c m??? kh??a user
		userService.blockAndUnblock(user.getId(), !user.isEnable());

		// reset login failed counter v??? 0
		authEntity = authService.findByUserId(user.getId());
		authEntity.setAuthenticationCounter(0);
		authService.save(authEntity);

		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "T??m ki???m th??ng tin role")
	@GetMapping("/admin/role")
	ResponseEntity<Pagingation<RoleDto>> viewRole(HttpServletRequest request, String roleName, @RequestParam(defaultValue = "1") int page) throws MyException, Exception {
		checkPermission(CommonConstant.VIEW_ROLE_PER, TokenProvider.getUsername(request));
		
		Page<Role> pageRole = null;

		if (CommonUtils.isNull(roleName)) {
			pageRole = roleService.findAll(PageRequest.of(page - 1, CommonConstant.PAGE_SIZE));
		} else {
			pageRole = roleService.findByName(roleName, PageRequest.of(page - 1, CommonConstant.PAGE_SIZE));
		}
		
		List<RoleDto> roleDtos = new ArrayList<>();
		pageRole.stream().forEach(r -> {
			RoleDto dto = new RoleDto();
			BeanUtils.copyProperties(r, dto);
			roleDtos.add(dto);
		});
		
		return ResponseEntity
				.ok(new Pagingation<>(roleDtos, pageRole.getTotalElements(), pageRole.getTotalPages()));
	}

	@ApiOperation(value = "Th??ng tin role chi ti???t")
	@GetMapping("/admin/role/{roleId}")
	ResponseEntity<RoleDetailDto> viewDetailRole(HttpServletRequest request, @PathVariable long roleId) throws Exception {
		checkPermission(CommonConstant.VIEW_ROLE_PER, TokenProvider.getUsername(request));
		
		Role role = roleService.findById(roleId);
		RoleDetailDto roleDetailDto = new RoleDetailDto();
		BeanUtils.copyProperties(role, roleDetailDto);
		return ResponseEntity.ok(roleDetailDto);
	}
	
	@ApiOperation(value = "Th??m role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Th???t b???i") })
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

	@ApiOperation(value = "S???a t??n role")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Th???t b???i") })
	@PutMapping("/admin/role")
	ResponseEntity<Role> updateRole(HttpServletRequest request, @ApiParam(value = "id role", required = true) @RequestParam long id, RoleForm form)
			throws Exception {
		checkPermission(CommonConstant.UPDATE_ROLE_PER, TokenProvider.getUsername(request));
		
		Role role = roleService.findById(id);
		List<Permission> permissions = permissionService.findByIds(form.getPermissionIds());
		role.setRoleKey(CommonUtils.toSlug(form.getRoleName()));
		role.setPermissions(permissions);
		role.setUpdateDatetime(LocalDateTime.now());

		Role roleResult = roleService.save(role);
		return ResponseEntity.ok(roleResult);
	}

	@ApiOperation(value = "X??a role")
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
			perPage = permissionService.findByName(name, PageRequest.of(page, CommonConstant.PAGE_SIZE));
		} else {
			perPage = permissionService.findAll(PageRequest.of(page, CommonConstant.PAGE_SIZE));
		}
		
		return ResponseEntity.ok(new Pagingation<>(perPage.toList(), perPage.getTotalElements(), perPage.getTotalPages()));
	}
	
	/**
	 * Ki???m tra permission
	 * @param permissionKey
	 * @param username
	 * @throws Exception t??i kho???n kh??ng c?? quy???n s??? n??m l???i.
	 */
	private void checkPermission(String permissionKey, String username) throws Exception {
		User user = userService.findByUsername(username);
		List<Permission> permissions = user.getRole().getPermissions();
		
		boolean isMatch = permissions.stream().anyMatch(p -> permissionKey.equals(p.getPermissionKey()));
		if(!isMatch) {
			throw new Exception("Truy c???p th???t b???i, ("+permissionKey+")");
		}
	}
}
