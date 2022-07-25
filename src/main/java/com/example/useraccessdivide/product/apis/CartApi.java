package com.example.useraccessdivide.product.apis;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.common.constant.CommonConstant;
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.EmailUtil;
import com.example.useraccessdivide.product.dtos.CartHistoryDto;
import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.entities.Product;
import com.example.useraccessdivide.product.forms.CartHistoryForm;
import com.example.useraccessdivide.product.forms.CartProductForm;
import com.example.useraccessdivide.product.services.CartHistoryService;
import com.example.useraccessdivide.product.services.CartService;
import com.example.useraccessdivide.product.services.ProductService;
import com.example.useraccessdivide.user.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/cart")
@Api(value = "Cart api", tags = { "api giỏ hàng" })
public class CartApi {
	@Autowired
	CartService cartService;
	@Autowired
	CartHistoryService cartHistoryService;
	@Autowired
	UserService userService;
	@Autowired
	ProductService productService;

	@ApiOperation(value = "Xem giỏ hàng")
	@GetMapping
	ResponseEntity<Pagingation<CartProductDto>> view(long userId,
			@RequestParam(name = "page", defaultValue = "1") int currentPage) {
		return ResponseEntity
				.ok(cartService.findByUserId(userId, PageRequest.of(currentPage - 1, CommonConstant.PAGE_SIZE)));
	}

	@ApiOperation(value = "Thêm và sửa giỏ hàng")
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	ResponseEntity<?> addAndUpdate(long userId, long productId, int quantity) throws MyException {
		cartService.save(userId, productId, quantity);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation(value = "Xóa 1 item trong giỏ hàng")
	@DeleteMapping
	ResponseEntity<?> deleteOne(long userId, long productId) {
		cartService.delete(userId, productId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation(value = "Xóa tất cả item trong giỏ hàng")
	@DeleteMapping("/delete-all")
	ResponseEntity<?> deleteAll(long userId) {
		cartService.delete(userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@ApiOperation(value = "Thanh toán", notes = "chưa tích hợp api thanh toán.")
	@PostMapping("/pay/{userId}")
	ResponseEntity<?> pay(@PathVariable long userId, @RequestBody List<CartProductForm> forms) throws MyException {
		// xóa giỏ hàng
		cartService.delete(userId);

		// thêm vào lịch sử (CartHistoryForm)
		List<Long> cardProductIds = forms.stream().map(item -> item.getProductId()).collect(Collectors.toList());
		List<Product> productInCart = productService.findAllById(cardProductIds);

		List<CartHistoryForm> cartHistoryForms = productInCart.stream().map(p -> {
			// lấy số lượng từ List<CartProductForm>
			int quantity = forms.stream().filter(item -> item.getProductId() == p.getId())
					.mapToInt(item -> item.getQuantity()).findFirst().getAsInt();
			CartHistoryForm form = new CartHistoryForm();
			form.setUserId(userId);
			form.setProductName(p.getName());
			form.setPrice(p.getPrice());
			form.setQuantity(quantity);
			form.setAddress("AAA");
			return form;
		}).collect(Collectors.toList());

		cartHistoryService.saveAll(cartHistoryForms);

		// gửi mail cho khách (CartHistoryDto)
		List<CartHistoryDto> cartHistoryDtos = cartHistoryForms.stream()
				.map(item -> {
					CartHistoryDto cartHistoryDto = new CartHistoryDto();
					BeanUtils.copyProperties(item, cartHistoryDto);
					cartHistoryDto.setBoughtDatetime(LocalDateTime.now());
					return cartHistoryDto;
				})
				.collect(Collectors.toList());
		
		long total = cartHistoryDtos.stream()
				.reduce(0, (base, value) ->  base + value.getPrice()*value.getQuantity(), Integer::sum);
//		long total = 0;
//		for (CartProductForm e : forms) {
//			Product product = productService.findById(e.getProductId());
//			cartHistoryDtos
//					.add(CartHistoryDto.builder().productName(product.getName()).price(product.getPrice())
//							.quantity(e.getQuantity()).boughtDatetime(LocalDateTime.now()).build());
//			total += product.getPrice() * e.getQuantity();
//		}
		Map<String, Object> map = new HashMap<>();
		map.put("productList", cartHistoryDtos);
		map.put("total", total);
		String email = userService.findById(userId).getEmail();
		try {
			EmailUtil.sendHtmlMail(email, "Danh sách các sản phẩm đã thanh toán",
					EmailUtil.readHtmlTemplateFile("product", map));
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
