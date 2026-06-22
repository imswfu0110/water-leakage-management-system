package com.donghai.leakage.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserRequest {
    @NotBlank(message = "账号不能为空") private String username;
    @NotBlank(message = "用户名不能为空") private String nickname;
    private String password;
    private String phone;
    @Email(message = "邮箱格式不正确") private String email;
    private Integer status = 1;
    private List<Long> roleIds = new ArrayList<>();
}
