package com.example.useraccessdivide.user.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter
public class User implements Serializable{
	private static final long serialVersionUID = -867098389955441480L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	@SequenceGenerator(sequenceName = "user_seq", name = "user_seq", allocationSize = 1)
	private Long id;
	@Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private boolean enable;
    
	@Column(nullable = false)
	private LocalDateTime createDatetime;
	@Column
	private LocalDateTime updateDatetime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
    
    @Override
    public String toString() {
    	String msg = "id= %s, username= %s, first name= %s, last name= %s, email= %s, enable= %s";
    	return String.format(msg, id, username, firstName, lastName, email, enable);
    }
}
