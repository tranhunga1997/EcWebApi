package com.example.useraccessdivide.product.dtos;

import java.time.LocalDateTime;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
	private Long id;
	private String thumbnailUrl;
	private String name;
	private String slug;
	private int price;
	private String shortDescription;
	private String description;
	private long categoryId;
	private String category;
	private long brandId;
	private String brand;
	private LocalDateTime startDatetime;
	private LocalDateTime endDatetime;
}
