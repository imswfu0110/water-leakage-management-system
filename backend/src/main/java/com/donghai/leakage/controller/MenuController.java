package com.donghai.leakage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.donghai.leakage.common.*;
import com.donghai.leakage.entity.SysMenu;
import com.donghai.leakage.entity.SysRoleMenu;
import com.donghai.leakage.mapper.*;
import com.donghai.leakage.service.MenuTreeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import com.donghai.leakage.security.RequirePermission;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    private final SysMenuMapper mapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final MenuTreeService treeService;

    public MenuController(SysMenuMapper mapper, SysRoleMenuMapper roleMenuMapper, MenuTreeService treeService) {
        this.mapper = mapper; this.roleMenuMapper = roleMenuMapper; this.treeService = treeService;
    }

    @GetMapping("/tree") @RequirePermission("system:menu:list") public Result<List<SysMenu>> tree() { return Result.success(treeService.allTree()); }
    @GetMapping("/user") public Result<List<SysMenu>> user(HttpServletRequest request) {
        return Result.success(treeService.userTree((Long) request.getAttribute("currentUserId")));
    }

    @PostMapping @Transactional @RequirePermission("system:menu:add")
    public Result<Void> create(@RequestBody SysMenu menu) {
        validate(menu, null); menu.setId(null); menu.setDeleted(0); menu.setCreateTime(LocalDateTime.now()); menu.setUpdateTime(LocalDateTime.now());
        mapper.insert(menu);
        return Result.success();
    }

    @PutMapping("/{id}") @RequirePermission("system:menu:edit")
    public Result<Void> update(@PathVariable Long id, @RequestBody SysMenu menu) {
        if (mapper.selectById(id) == null) throw new BusinessException(404, "菜单不存在");
        validate(menu, id); menu.setId(id); menu.setUpdateTime(LocalDateTime.now()); mapper.updateById(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}") @Transactional @RequirePermission("system:menu:delete")
    public Result<Void> delete(@PathVariable Long id) {
        if (mapper.selectCount(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id)) > 0) throw new BusinessException("请先删除子菜单");
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, id));
        mapper.deleteById(id); return Result.success();
    }

    private void validate(SysMenu menu, Long id) {
        if (menu.getMenuName() == null || menu.getMenuName().isBlank()) throw new BusinessException("菜单名称不能为空");
        if (!List.of("CATALOG", "MENU", "BUTTON").contains(menu.getMenuType())) throw new BusinessException("菜单类型不正确");
        unique(SysMenu::getPath, menu.getPath(), id, "路由路径已存在");
        unique(SysMenu::getComponent, menu.getComponent(), id, "组件路径已存在");
        unique(SysMenu::getPermissionCode, menu.getPermissionCode(), id, "权限标识已存在");
        if (menu.getParentId() == null) menu.setParentId(0L);
        if (menu.getParentId() != 0 && mapper.selectById(menu.getParentId()) == null) throw new BusinessException("父级菜单不存在");
        if (id != null && isDescendant(menu.getParentId(), id)) throw new BusinessException("不能选择自身或子菜单作为父级");
        if (depth(menu.getParentId()) >= 3) throw new BusinessException("菜单最多支持三级");
        if (id != null && id.equals(menu.getParentId())) throw new BusinessException("不能选择自身作为父菜单");
        if (menu.getSortOrder() == null) menu.setSortOrder(0);
        if (menu.getVisible() == null) menu.setVisible(1);
        if (menu.getStatus() == null) menu.setStatus(1);
    }

    private <T> void unique(com.baomidou.mybatisplus.core.toolkit.support.SFunction<SysMenu,T> column, T value, Long id, String message) {
        if (value == null || value.toString().isBlank()) return;
        LambdaQueryWrapper<SysMenu> q = new LambdaQueryWrapper<SysMenu>().eq(column, value);
        if (id != null) q.ne(SysMenu::getId, id);
        if (mapper.selectCount(q) > 0) throw new BusinessException(message);
    }

    private int depth(Long parentId) {
        int depth = 0;
        while (parentId != null && parentId != 0 && depth < 4) {
            SysMenu parent = mapper.selectById(parentId); if (parent == null) break; parentId = parent.getParentId(); depth++;
        }
        return depth;
    }

    private boolean isDescendant(Long parentId, Long id) {
        int guard = 0;
        while (parentId != null && parentId != 0 && guard++ < 10) {
            if (parentId.equals(id)) return true;
            SysMenu parent = mapper.selectById(parentId);
            if (parent == null) return false;
            parentId = parent.getParentId();
        }
        return false;
    }
}
