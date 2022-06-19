package com.example.useraccessdivide.product.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartItemPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long userId;
	private Long productId;

}
