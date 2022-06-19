package com.example.useraccessdivide.user.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "authentications")
public class AuthenticationsEntity implements Serializable {

	private static final long serialVersionUID = 3731825700751979127L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_seq")
	@SequenceGenerator(sequenceName = "auth_seq", name = "auth_seq", allocationSize = 1)
	private int id;
	@Column(nullable = false)
	private int historyId;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private int authenticationCounter;
	@Column(nullable = false)
	private LocalDateTime createDatetime;
	@Column(nullable = true)
	private LocalDateTime deleteDatetime;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User uzer;
}
