package com.example.useraccessdivide.product.forms;

import com.example.useraccessdivide.product.entities.ProductEntity;

import lombok.Data;

@Data
public class CartProductForm {
    private long productId;
    private int quantity;
}
