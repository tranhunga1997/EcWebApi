package com.example.useraccessdivide.product.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.dtos.ProductDetailDto;
import com.example.useraccessdivide.product.entities.CartHistory;
import com.example.useraccessdivide.product.entities.CartItem;
import com.example.useraccessdivide.product.entities.Product;
import com.example.useraccessdivide.product.forms.CartHistoryForm;
import com.example.useraccessdivide.product.repositories.CartHistoryRepository;
import com.example.useraccessdivide.product.repositories.CartRepository;
import com.example.useraccessdivide.user.services.UserService;

@Service
public class CartService {
	@PersistenceContext
	EntityManager em;
	
	@Autowired
	CartRepository cartRepository;
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;
	@Autowired
	CartHistoryRepository cartHistoryRepository;

	/**
	 * Hiển thị giỏ hàng
	 * 
	 * @param userId
	 * @param currentPage
	 * @return
	 */
	public Pagingation<CartProductDto> findByUserId(long userId, Pageable pageable) {
		Page<CartItem> cartItemPage = cartRepository.findByUserId(userId, pageable);
		List<Product> products = productService.findAllById(cartItemPage.stream().map(CartItem::getProductId).collect(Collectors.toList()));
		
		// convert list CartItem > list CartProductDto
		List<CartProductDto> cartProductDtos = cartItemPage.stream()
				.map(value -> {
					CartProductDto dto = new CartProductDto();
					ProductDetailDto pDto = new ProductDetailDto();
					BeanUtils.copyProperties(products.stream().filter(p -> p.getId() == value.getProductId()), pDto);
					dto.setUserId(value.getUserId());
					dto.setProductInfo(pDto);
					dto.setQuantity(value.getQuantity());
					return dto;
				})
				.collect(Collectors.toList());
		return new Pagingation<CartProductDto>(cartProductDtos, cartItemPage.getTotalElements(), cartItemPage.getTotalPages());
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
	 * @return thông tin sản phẩm đã xóa trong giỏ
	 * <b>Vấn đề: sử dụng transaction</b>
	 */
	public List<CartItem> delete(long userId) {
		List<CartItem> cartItems = cartRepository.findByUserId(userId);
		// thêm vào lịch sử (CartHistoryForm)
		List<Long> cardProductIds = cartItems.stream().map(item -> item.getProductId()).collect(Collectors.toList());
		List<Product> productInCart = productService.findAllById(cardProductIds);

		List<CartHistory> cartHistorys = productInCart.stream().map(p -> {
			// lấy số lượng từ List<CartItem>
			int quantity = cartItems.stream().filter(item -> item.getProductId() == p.getId())
					.mapToInt(item -> item.getQuantity()).findFirst().getAsInt();
			CartHistory cartHistory = new CartHistory();
			cartHistory.setUserId(userId);
			cartHistory.setProductName(p.getName());
			cartHistory.setPrice(p.getPrice());
			cartHistory.setQuantity(quantity);
			cartHistory.setAddress("AAA"); // address sau này sửa lại
			cartHistory.setBoughtDatetime(LocalDateTime.now());
			return cartHistory;
		}).collect(Collectors.toList());
		cartHistoryRepository.saveAll(cartHistorys);
		// xóa giỏ hàng
		cartRepository.deleteByUserId(userId);
		return cartItems;
	}

}
