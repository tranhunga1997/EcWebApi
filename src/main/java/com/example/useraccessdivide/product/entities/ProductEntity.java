package com.example.useraccessdivide.product.entities;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class ProductEntity extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "products_seq")
	private long id;
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
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;
    @Column(nullable = false)
    private LocalDateTime startDatetime;
    @Column
    private LocalDateTime endDatetime;
}
