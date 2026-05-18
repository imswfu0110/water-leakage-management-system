package com.donghai.leakage.service.impl;

import com.donghai.leakage.common.BusinessException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.donghai.leakage.dto.LoginRequest;
import com.donghai.leakage.entity.SysUser;
import com.donghai.leakage.mapper.SysUserMapper;
import com.donghai.leakage.service.AuthService;
import com.donghai.leakage.vo.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper sysUserMapper;

    public AuthServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new RuntimeException("账号或密码不能为空");
        }

        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, request.getUsername())
                        .eq(SysUser::getDeleted, 0)
                        .last("LIMIT 1")
        );

        if (user == null) {
            throw new BusinessException("账号不存在");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new RuntimeException("账号已被禁用");
        }

        // 当前数据库 admin 密码123456
        // 明文判断
        if (!"123456".equals(request.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        LoginResponse response = new LoginResponse();
        response.setToken("dev-token-" + user.getId());
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setNickname(user.getNickname());

        return response;
    }
}