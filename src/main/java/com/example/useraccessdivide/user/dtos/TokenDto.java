package com.example.useraccessdivide.user.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class TokenDto implements Serializable{
	private static final long serialVersionUID = 3525837283151232997L;

    private String token;
    private Date tokenExpDate;
    private String username;
    private long updateBy;

    @Override
    public String toString(){
        String str = "( token: "+this.token+", tokenExpdate: "+this.tokenExpDate+", createBy: "+this.username+", updateBy: "+this.updateBy;
        return str;
    }
}
