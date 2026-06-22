package com.donghai.leakage.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SystemConfigRequest {
    @NotBlank(message = "系统名称不能为空") private String systemName;
    private String systemLogo;
}
