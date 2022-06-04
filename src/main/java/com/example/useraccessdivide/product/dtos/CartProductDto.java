package com.example.useraccessdivide.product.dtos;

import com.example.useraccessdivide.product.entities.ProductEntity;
import lombok.Data;

@Data
public class CartProductDto {
    private long userId;
    private ProductEntity productInfo;
    private int quantity;
}
