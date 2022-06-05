package com.example.useraccessdivide.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagingation<T> {
	private List<T> datas;
	private long totalElement;
	private int totalPage;
}
