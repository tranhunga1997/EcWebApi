package com.example.useraccessdivide.product.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.dtos.ProductDetailDto;
import com.example.useraccessdivide.product.entities.CartItem;
import com.example.useraccessdivide.product.repositories.CartRepository;
import com.example.useraccessdivide.user.services.UserService;

@Service
public class CartService {
	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;

	/**
	 * Hiển thị giỏ hàng
	 * 
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public Pagingation<CartProductDto> findByUserId(long userId, Pageable pageable) {
		Page<CartItem> page = cartRepository.findByUserId(userId, pageable);
		
		// convert list CartItem > list CartProductDto
		List<CartProductDto> cartProductDtos = page.stream()
				.map(value -> {
					CartProductDto dto = new CartProductDto();
					ProductDetailDto pDto = new ProductDetailDto();
					BeanUtils.copyProperties(value.getProduct(), pDto);
					dto.setUserId(value.getUserId());
					dto.setProductInfo(pDto);
					dto.setQuantity(value.getQuantity());
					return dto;
				})
				.collect(Collectors.toList());
		
		return new Pagingation<CartProductDto>(cartProductDtos, page.getTotalElements(), page.getTotalPages());
	}

	/**
	 * Thêm sản phẩm vào giỏ hàng
	 * 
	 * @param userId    id tài khoản
	 * @param productId mã sản phẩm
	 * @param quantity  số lượng
	 * @throws MyException
	 */
	public void save(long userId, long productId, int quantity) throws MyException {
		CartItem entity = new CartItem();
		entity.setUserId(userId);
		entity.setProductId(productId);
		entity.setQuantity(quantity);
		cartRepository.save(entity);
	}

	/**
	 * Xóa sản 1 sản phẩm
	 * @param userId
	 * @param productId
	 */
	public void delete(long userId, long productId) {
		cartRepository.deleteByUserIdAndProductId(userId, productId);
	}

	/**
	 * Xóa tất cả sản phẩm của 1 tài khoản
	 * @param userId
	 */
	public void delete(long userId) {
		cartRepository.deleteByUserId(userId);
	}

}
