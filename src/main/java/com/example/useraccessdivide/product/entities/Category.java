package com.example.useraccessdivide.product.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor @AllArgsConstructor
public class Category implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "categories_seq")
	@SequenceGenerator(sequenceName = "categories_seq", name = "categories_seq", allocationSize = 1)
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
    @Column(nullable = false)
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
}
