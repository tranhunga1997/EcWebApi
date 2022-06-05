package com.example.useraccessdivide.product.apis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.useraccessdivide.common.exception.MyException;
import com.example.useraccessdivide.common.utils.EmailUtil;
import com.example.useraccessdivide.product.dtos.CartHistoryDto;
import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.entities.ProductEntity;
import com.example.useraccessdivide.product.forms.CartHistoryForm;
import com.example.useraccessdivide.product.forms.CartProductForm;
import com.example.useraccessdivide.product.services.CartHistoryService;
import com.example.useraccessdivide.product.services.CartService;
import com.example.useraccessdivide.product.services.ProductService;
import com.example.useraccessdivide.user.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/cart")
@Api(value = "Cart api", tags = {"api giỏ hàng"})
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
    ResponseEntity<Pagingation<CartProductDto>> view(long userId, @RequestParam("page") int currentPage){
        return ResponseEntity.ok(cartService.findByUserId(userId, currentPage));
    }

    @ApiOperation(value = "Thêm và sửa giỏ hàng")
    @RequestMapping(method = {RequestMethod.POST,RequestMethod.PUT})
    ResponseEntity addAndUpdate(@RequestParam long userId, @RequestParam long productId, @RequestParam int quantity){
        cartService.save(userId, productId, quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @ApiOperation(value = "Xóa 1 item trong giỏ hàng")
    @DeleteMapping
    ResponseEntity deleteOne(@RequestParam long userId, @RequestParam long productId){
        cartService.delete(userId, productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "Xóa tất cả item trong giỏ hàng")
    @DeleteMapping("/delete-all")
    ResponseEntity deleteAll(@RequestParam long userId){
        cartService.delete(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @ApiOperation(value = "Thanh toán", notes = "chưa tích hợp api thanh toán.")
    @PostMapping("/pay/{userId}")
    ResponseEntity pay(@PathVariable long userId, @RequestBody List<CartProductForm> forms) throws MyException {
    	// xóa giỏ hàng 
    	cartService.delete(userId);
    	
    	// thêm vào lịch sử (CartHistoryForm)
    	List<CartHistoryForm> cartHistoryForms = new ArrayList<CartHistoryForm>();
    	forms.forEach(e -> {
    		CartHistoryForm form = new CartHistoryForm();
    		ProductEntity productEntity = productService.findById(e.getProductId()).get();
    		form.setUserId(userId);
    		form.setProductName(productEntity.getName());
    		form.setPrice(productEntity.getPrice());
    		form.setQuantity(e.getQuantity());
    		form.setAddress("AAA");
    		cartHistoryForms.add(form);
    	});
    	cartHistoryService.saveAll(cartHistoryForms);
    	
    	// gửi mail cho khách (CartHistoryDto)
    	List<CartHistoryDto> cartHistoryDtos = new ArrayList<CartHistoryDto>();
    	long total = 0;
    	for(CartProductForm e : forms) {
    		ProductEntity productEntity = productService.findById(e.getProductId()).get();
    		cartHistoryDtos.add(CartHistoryDto.builder()
    				.productName(productEntity.getName())
    				.price(productEntity.getPrice())
    				.quantity(e.getQuantity())
    				.boughtDatetime(LocalDateTime.now())
    				.build()
    				);
    		total += productEntity.getPrice()*e.getQuantity();
    	}
    	Map<String, Object> map = new HashMap<>();
    	map.put("productList", cartHistoryDtos);
    	map.put("total", total);
    	String email = userService.findById(userId).getEmail();
    	try {
			EmailUtil.sendHtmlMail(email, "Danh sách các sản phẩm đã thanh toán", EmailUtil.readHtmlTemplateFile("product", map));
		} catch (MessagingException e1) {
			e1.printStackTrace();
		}
    	return ResponseEntity.status(HttpStatus.OK).build();
    }


}
