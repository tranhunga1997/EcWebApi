package com.example.useraccessdivide.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartHistoryDto {
	private long productId;
    private String productName;
    private int price;
    private int quantity;
    private LocalDateTime boughtDatetime;
}
