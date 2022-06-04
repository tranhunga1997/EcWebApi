package com.example.useraccessdivide.product.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDataDto<T> {
	private List<T> datas;
	private long totalElement;
	private int totalPage;
}
