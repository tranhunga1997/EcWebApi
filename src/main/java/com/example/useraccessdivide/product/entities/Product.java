package com.example.useraccessdivide.product.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
	@SequenceGenerator(sequenceName = "product_seq", name = "product_seq", allocationSize = 1)
	private Long id;
	@Column
	private String thumbnailUrl;
	@Column(length = 100, nullable = false)
	private String name;
	@Column(unique = true, nullable = false)
	private String slug;
	@Column(nullable = false)
	private int price;
	@Column
	private String shortDescription;
	@Column(length = 50000)
	private String description;
	@Column(nullable = false)
	private LocalDateTime startDatetime;
	@Column(name = "category_id", nullable = false)
	long categoryId;
	@Column(name = "brand_id", nullable = false)
	long brandId;
	private LocalDateTime endDatetime;
	@Column(nullable = false)
	private LocalDateTime createDatetime;
	@Column
	private LocalDateTime updateDatetime;

	// Relationship
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private Category category;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", insertable = false, updatable = false)
	private Brand brand;
}
