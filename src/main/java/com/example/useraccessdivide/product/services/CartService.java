package com.example.useraccessdivide.product.services;

import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.dtos.PaginationDataDto;
import com.example.useraccessdivide.product.entities.CartItemEntity;
import com.example.useraccessdivide.product.repositories.CartRepository;
import com.example.useraccessdivide.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;

    public PaginationDataDto<CartProductDto> findByUserId(long userId, int currentPage){
        List<CartProductDto> cartProductDtos = new ArrayList<>();
        Page<CartItemEntity> page = cartRepository.findByUserId(userId, PageRequest.of(currentPage, 5));
        page.forEach(e -> {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(e.getUser().getId());
            cartProductDto.setProductInfo(e.getProduct());
            cartProductDto.setQuantity(e.getQuantity());
            cartProductDtos.add(cartProductDto);
        });
        PaginationDataDto<CartProductDto> dataDto = new PaginationDataDto<CartProductDto>(cartProductDtos, page.getTotalElements(), page.getTotalPages());
        return dataDto;
    }
    // code day
    public void save(long userId, long productId, int quantity){
        try {
            CartItemEntity entity = new CartItemEntity();
            entity.setUser(userService.findById(userId).get());
            entity.setProduct(productService.findById(productId).get());
            entity.setQuantity(quantity);
            cartRepository.save(entity);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(long userId, long productId){
        cartRepository.deleteByUserIdAndProductId(userId,productId);
    }

    public void delete(long userId){
        cartRepository.deleteByUserId(userId);
    }
   
}
