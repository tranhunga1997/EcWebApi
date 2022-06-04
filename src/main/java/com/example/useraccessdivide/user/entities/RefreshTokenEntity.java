package com.example.useraccessdivide.user.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "login_refresh_tokens")
@Data
public class RefreshTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String refreshToken;
	@Column(length = 15)
	private String ipAddress;
	@Column
	private Long userId;
	@Column
	private LocalDateTime expiryDatetime;
	@Column
	private LocalDateTime createDatetime = LocalDateTime.now();
	@Column()
	private LocalDateTime updateDatetime;
	
	public void setExpiryDatetime(int day) {
		this.expiryDatetime = LocalDateTime.now().plusDays(day);
	}
}
