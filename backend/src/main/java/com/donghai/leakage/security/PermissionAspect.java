package com.donghai.leakage.security;

import com.donghai.leakage.common.BusinessException;
import com.donghai.leakage.entity.SysMenu;
import com.donghai.leakage.mapper.SysMenuMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class PermissionAspect {
    private final SysMenuMapper menuMapper;
    public PermissionAspect(SysMenuMapper menuMapper) { this.menuMapper = menuMapper; }

    @Before("@annotation(permission)")
    public void check(RequirePermission permission) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Long userId = (Long) request.getAttribute("currentUserId");
        boolean allowed = menuMapper.selectByUserId(userId).stream().map(SysMenu::getPermissionCode)
                .anyMatch(permission.value()::equals);
        if (!allowed) throw new BusinessException(403, "无权限执行此操作");
    }
}
