package com.donghai.leakage.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginRequest {

    @NotBlank(message = "请输入账号")
    private String username;

    @NotBlank(message = "请输入密码")
    private String password;

    @NotBlank(message = "请获取验证码")
    private String captchaId;

    @NotBlank(message = "请输入验证码")
    private String captchaCode;
}
