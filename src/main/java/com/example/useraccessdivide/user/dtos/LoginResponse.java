package com.example.useraccessdivide.user.dtos;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private static final String tokenType = "Bearer ";
    private String msg;

    public LoginResponse(String accessToken, String msg) {
        this.accessToken = accessToken;
        this.msg = msg;
    }

}
