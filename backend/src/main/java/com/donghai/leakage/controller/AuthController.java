package com.donghai.leakage.controller;

import com.donghai.leakage.common.Result;
import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.service.AuthService;
import com.donghai.leakage.vo.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("auth module ok");
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }
}