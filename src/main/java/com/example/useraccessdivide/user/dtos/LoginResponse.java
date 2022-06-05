package com.example.useraccessdivide.user.dtos;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private final String tokenType = "Bearer ";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
