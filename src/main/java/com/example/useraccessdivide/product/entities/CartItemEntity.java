package com.example.useraccessdivide.product.entities;


import com.example.useraccessdivide.user.entities.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cart_items")
@Data
public class CartItemEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(nullable = false)
    private int quantity;
}
