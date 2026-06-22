package com.donghai.leakage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.donghai.leakage.common.BusinessException;
import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.entity.SysMenu;
import com.donghai.leakage.entity.SysRole;
import com.donghai.leakage.entity.SysUser;
import com.donghai.leakage.mapper.SysMenuMapper;
import com.donghai.leakage.mapper.SysRoleMapper;
import com.donghai.leakage.mapper.SysUserMapper;
import com.donghai.leakage.security.TokenService;
import com.donghai.leakage.service.AuthService;
import com.donghai.leakage.service.CaptchaService;
import com.donghai.leakage.vo.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysMenuMapper menuMapper;
    private final CaptchaService captchaService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(SysUserMapper userMapper, SysRoleMapper roleMapper, SysMenuMapper menuMapper,
                           CaptchaService captchaService, TokenService tokenService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.menuMapper = menuMapper;
        this.captchaService = captchaService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        captchaService.verify(request.getCaptchaId(), request.getCaptchaCode());
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, request.getUsername()).eq(SysUser::getDeleted, 0).last("LIMIT 1"));
        if (user == null) throw new BusinessException("账号不存在");
        if (user.getStatus() == null || user.getStatus() != 1) throw new BusinessException("账号已被禁用");

        String stored = user.getPassword();
        boolean legacy = stored != null && !stored.startsWith("$2");
        boolean matches = legacy ? stored.equals(request.getPassword()) : passwordEncoder.matches(request.getPassword(), stored);
        if (!matches) throw new BusinessException("密码错误");
        if (legacy) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userMapper.updateById(user);
        }

        LoginResponse response = new LoginResponse();
        response.setToken(tokenService.create(user.getId()));
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());
        return response;
    }

    @Override
    public Map<String, String> captcha() {
        return captchaService.create();
    }

    @Override
    public Map<String, Object> userInfo(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1 || user.getStatus() != 1) throw new BusinessException(401, "用户不存在或已禁用");
        List<SysRole> roles = roleMapper.selectByUserId(userId);
        List<String> permissions = menuMapper.selectByUserId(userId).stream()
                .map(SysMenu::getPermissionCode).filter(s -> s != null && !s.isBlank()).distinct().toList();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("roles", roles.stream().map(SysRole::getRoleCode).toList());
        result.put("permissions", permissions);
        return result;
    }
}
