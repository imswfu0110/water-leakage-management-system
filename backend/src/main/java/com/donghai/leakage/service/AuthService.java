package com.donghai.leakage.service;

import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.vo.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}