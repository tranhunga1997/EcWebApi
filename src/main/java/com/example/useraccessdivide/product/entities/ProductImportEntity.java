package com.example.useraccessdivide.product.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_imports")
@Data
public class ProductImportEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private LocalDateTime storingDatetime;
}
