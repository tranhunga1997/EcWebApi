package com.example.useraccessdivide.product.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ware_houses")
@Data
public class WareHouse implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column
    private int onMonthQuantity;
    @Column
    private int onMonthImport;
    @Column
    private int onMonthExport;
    @Column
    private LocalDateTime createDatetime;
}
