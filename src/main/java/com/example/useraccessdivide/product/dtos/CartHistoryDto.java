package com.example.useraccessdivide.product.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CartHistoryDto {
    private String productName;
    private int price;
    private int quantity;
    private LocalDateTime boughtDatetime;
}
