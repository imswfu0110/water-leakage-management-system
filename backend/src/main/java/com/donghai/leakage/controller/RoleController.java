package com.donghai.leakage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.donghai.leakage.common.*;
import com.donghai.leakage.dto.IdListRequest;
import com.donghai.leakage.entity.*;
import com.donghai.leakage.mapper.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import com.donghai.leakage.security.RequirePermission;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    private final SysRoleMapper mapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserRoleMapper userRoleMapper;
    public RoleController(SysRoleMapper mapper, SysRoleMenuMapper roleMenuMapper, SysUserRoleMapper userRoleMapper) {
        this.mapper=mapper; this.roleMenuMapper=roleMenuMapper; this.userRoleMapper=userRoleMapper;
    }

    @GetMapping("/page")
    @RequirePermission("system:role:list")
    public Result<Page<SysRole>> page(@RequestParam(defaultValue="1") long pageNum, @RequestParam(defaultValue="10") long pageSize,
                                     @RequestParam(defaultValue="") String roleName) {
        return Result.success(mapper.selectPage(Page.of(pageNum, pageSize), new LambdaQueryWrapper<SysRole>()
                .like(!roleName.isBlank(), SysRole::getRoleName, roleName).orderByAsc(SysRole::getId)));
    }
    @GetMapping("/list") @RequirePermission("system:role:list") public Result<List<SysRole>> list() { return Result.success(mapper.selectList(new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus,1).orderByAsc(SysRole::getId))); }

    @PostMapping @RequirePermission("system:role:add") public Result<Void> create(@RequestBody SysRole role) {
        validate(role,null); role.setId(null); role.setDeleted(0); role.setCreateTime(LocalDateTime.now()); role.setUpdateTime(LocalDateTime.now()); mapper.insert(role); return Result.success();
    }
    @PutMapping("/{id}") @RequirePermission("system:role:edit") public Result<Void> update(@PathVariable Long id,@RequestBody SysRole role) {
        validate(role,id); role.setId(id); role.setUpdateTime(LocalDateTime.now()); mapper.updateById(role); return Result.success();
    }
    @DeleteMapping("/{id}") @Transactional @RequirePermission("system:role:delete") public Result<Void> delete(@PathVariable Long id) {
        SysRole role=mapper.selectById(id); if(role==null) throw new BusinessException(404,"角色不存在");
        if("admin".equals(role.getRoleCode())) throw new BusinessException("系统管理员角色不能删除");
        if(userRoleMapper.selectCount(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId,id))>0) throw new BusinessException("该角色已被用户使用，请先解除绑定");
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId,id)); mapper.deleteById(id); return Result.success();
    }
    @GetMapping("/{id}/menus") @RequirePermission("system:role:list") public Result<List<Long>> menus(@PathVariable Long id) {
        return Result.success(roleMenuMapper.selectList(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId,id)).stream().map(SysRoleMenu::getMenuId).toList());
    }
    @PutMapping("/{id}/menus") @Transactional @RequirePermission("system:role:edit") public Result<Void> menus(@PathVariable Long id,@RequestBody IdListRequest request) {
        if(mapper.selectById(id)==null) throw new BusinessException(404,"角色不存在");
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId,id));
        for(Long menuId:request.getMenuIds()){ SysRoleMenu x=new SysRoleMenu(); x.setRoleId(id); x.setMenuId(menuId); roleMenuMapper.insert(x); }
        return Result.success();
    }
    private void validate(SysRole role,Long id){
        if(role.getRoleName()==null||role.getRoleName().isBlank()||role.getRoleCode()==null||role.getRoleCode().isBlank()) throw new BusinessException("角色名称和编码不能为空");
        LambdaQueryWrapper<SysRole> q=new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode,role.getRoleCode()); if(id!=null)q.ne(SysRole::getId,id);
        if(mapper.selectCount(q)>0)throw new BusinessException("角色编码已存在"); if(role.getStatus()==null)role.setStatus(1);
    }
}
