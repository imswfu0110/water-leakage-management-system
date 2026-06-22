package com.donghai.leakage.controller;

import com.donghai.leakage.common.Result;
import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.service.AuthService;
import com.donghai.leakage.vo.LoginResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

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
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @GetMapping("/captcha")
    public Result<Map<String, String>> captcha() {
        return Result.success(authService.captcha());
    }

    @GetMapping("/userinfo")
    public Result<Map<String, Object>> userInfo(HttpServletRequest request) {
        return Result.success(authService.userInfo((Long) request.getAttribute("currentUserId")));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }
}
