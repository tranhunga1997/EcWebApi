package com.example.useraccessdivide.product.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ware_houses")
@Data
public class WareHouse implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ware_house_seq")
	@SequenceGenerator(sequenceName = "ware_house_seq", name = "ware_house_seq", allocationSize = 1)
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column
    private int onMonthQuantity;
    @Column
    private int onMonthImport;
    @Column
    private int onMonthExport;
    @Column
    private LocalDateTime createDatetime;
}
