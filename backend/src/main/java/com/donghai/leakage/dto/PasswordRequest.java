package com.donghai.leakage.dto;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordRequest {
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 50, message = "密码长度应为 6-50 位")
    private String password;
}
