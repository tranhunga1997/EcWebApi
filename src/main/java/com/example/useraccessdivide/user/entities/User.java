package com.example.useraccessdivide.user.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter @Setter
public class User extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -867098389955441480L;
	@Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private boolean enable;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
    
    @Override
    public String toString() {
    	String msg = "id= %s, username= %s, first name= %s, last name= %s, email= %s, enable= %s";
    	return String.format(msg, super.getId(), username, firstName, lastName, email, enable);
    }
}
