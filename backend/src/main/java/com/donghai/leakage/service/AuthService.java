package com.donghai.leakage.service;

import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.vo.LoginResponse;
import java.util.Map;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    Map<String, String> captcha();

    Map<String, Object> userInfo(Long userId);
}
