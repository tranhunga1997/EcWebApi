package com.example.useraccessdivide.user.entities.pk;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.OneToMany;

import com.example.useraccessdivide.user.entities.User;

import lombok.Data;

@Data
public class AuthenticationPK implements Serializable {
	private static final long serialVersionUID = -2275641724853436473L;
	private Collection<User> uzer;
	private int historyId;
}
