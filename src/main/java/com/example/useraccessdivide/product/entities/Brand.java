package com.example.useraccessdivide.product.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "brands")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Brand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brands_seq")
	@SequenceGenerator(sequenceName = "brands_seq", name = "brands_seq", allocationSize = 1)
	private long id;
    @Column(nullable = false, length = 50)
    @NonNull
    private String name;
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(nullable = false)
    private LocalDateTime createDatetime;
    private LocalDateTime updateDatetime;
}
