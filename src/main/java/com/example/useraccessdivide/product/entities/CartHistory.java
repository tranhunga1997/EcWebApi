package com.example.useraccessdivide.product.entities;

import com.example.useraccessdivide.user.entities.User;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_histories")
@Data
public class CartHistory implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_history_seq")
	@SequenceGenerator(sequenceName = "cart_history_seq", name = "cart_history_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private long userId;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private LocalDateTime boughtDatetime;
}
