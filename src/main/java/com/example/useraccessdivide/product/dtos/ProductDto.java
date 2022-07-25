package com.example.useraccessdivide.product.dtos;

import lombok.Data;

@Data
public class ProductDto {
	private long id;
	private String thumbnailUrl;
	private String name;
	private int price;
	private String shortDescription;
}
