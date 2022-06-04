package com.example.useraccessdivide.product.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor @AllArgsConstructor
public class CategoryEntity extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "categories_seq")
	private long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String slug;
    @Column
    private float tax;
    @Column
    private int parentId;
    @Column(nullable = false)
    private int levelId;
    @Column(nullable = false)
    private int orderId;
}
