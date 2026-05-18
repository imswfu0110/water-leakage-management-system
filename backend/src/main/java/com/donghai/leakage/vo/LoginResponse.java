package com.donghai.leakage.vo;

import lombok.Data;

@Data
public class LoginResponse {

    private String token;

    private Long userId;

    private String username;

    private String nickname;
}