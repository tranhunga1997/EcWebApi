package com.example.useraccessdivide.product.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.useraccessdivide.common.Pagingation;
import com.example.useraccessdivide.product.dtos.CartProductDto;
import com.example.useraccessdivide.product.entities.CartItemEntity;
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

    public Pagingation<CartProductDto> findByUserId(long userId, int currentPage){
        List<CartProductDto> cartProductDtos = new ArrayList<>();
        Page<CartItemEntity> page = cartRepository.findByUserId(userId, PageRequest.of(currentPage, 5));
        page.forEach(e -> {
            CartProductDto cartProductDto = new CartProductDto();
            cartProductDto.setUserId(e.getUser().getId());
            cartProductDto.setProductInfo(e.getProduct());
            cartProductDto.setQuantity(e.getQuantity());
            cartProductDtos.add(cartProductDto);
        });
        Pagingation<CartProductDto> dataDto = new Pagingation<CartProductDto>(cartProductDtos, page.getTotalElements(), page.getTotalPages());
        return dataDto;
    }
    // code day
    public void save(long userId, long productId, int quantity){
        try {
            CartItemEntity entity = new CartItemEntity();
            entity.setUser(userService.findById(userId));
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
