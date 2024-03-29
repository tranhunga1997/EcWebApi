package com.example.useraccessdivide.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartProductDto {
    private long userId;
    private ProductDetailDto productInfo;
    private int quantity;
}
