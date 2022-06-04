package com.example.useraccessdivide.product.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "brands")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BrandEntity extends BaseEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "brands_seq")
	private long id;
    @Column(nullable = false, length = 50)
    @NonNull
    private String name;
    @Column(nullable = false, unique = true)
    private String slug;
}
