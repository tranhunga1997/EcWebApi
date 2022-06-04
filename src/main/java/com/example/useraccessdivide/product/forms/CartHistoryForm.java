package com.example.useraccessdivide.product.forms;

import lombok.Data;

import javax.persistence.Column;

@Data
public class CartHistoryForm {
    private long userId;
    private String productName;
    private int price;
    private int quantity;
    private String address;
}
