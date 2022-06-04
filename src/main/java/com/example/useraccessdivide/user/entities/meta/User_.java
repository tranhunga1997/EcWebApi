package com.example.useraccessdivide.user.entities.meta;

import com.example.useraccessdivide.user.entities.User;

import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public final class User_ {
    public static final String USERNAME = "username";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String EMAIL = "email";
    public static final String ENABLE = "enable";
}
