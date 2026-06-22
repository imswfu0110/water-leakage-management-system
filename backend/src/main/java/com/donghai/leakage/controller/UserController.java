package com.donghai.leakage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.donghai.leakage.common.*;
import com.donghai.leakage.dto.*;
import com.donghai.leakage.entity.*;
import com.donghai.leakage.mapper.*;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import com.donghai.leakage.security.RequirePermission;

@RestController
@RequestMapping("/system/user")
public class UserController {
    private final SysUserMapper mapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysRoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(SysUserMapper mapper, SysUserRoleMapper userRoleMapper, SysRoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.mapper=mapper; this.userRoleMapper=userRoleMapper; this.roleMapper=roleMapper; this.passwordEncoder=passwordEncoder;
    }

    @GetMapping("/page")
    @RequirePermission("system:user:list")
    public Result<Map<String,Object>> page(@RequestParam(defaultValue="1") long pageNum,@RequestParam(defaultValue="10") long pageSize,
                                           @RequestParam(defaultValue="") String username,@RequestParam(defaultValue="") String nickname){
        Page<SysUser> page=mapper.selectPage(Page.of(pageNum,pageSize),new LambdaQueryWrapper<SysUser>()
                .like(!username.isBlank(),SysUser::getUsername,username).like(!nickname.isBlank(),SysUser::getNickname,nickname)
                .eq(SysUser::getDeleted,0).orderByAsc(SysUser::getId));
        List<Map<String,Object>> records=page.getRecords().stream().map(this::view).toList();
        Map<String,Object> result=new LinkedHashMap<>(); result.put("records",records); result.put("total",page.getTotal()); result.put("current",page.getCurrent()); result.put("size",page.getSize());
        return Result.success(result);
    }

    @PostMapping @Transactional @RequirePermission("system:user:add")
    public Result<Void> create(@Valid @RequestBody UserRequest request){
        validateUnique(request,null); if(request.getPassword()==null||request.getPassword().length()<6)throw new BusinessException("初始密码不能少于 6 位");
        SysUser user=toEntity(request); user.setPassword(passwordEncoder.encode(request.getPassword())); user.setDeleted(0); user.setCreateTime(LocalDateTime.now()); user.setUpdateTime(LocalDateTime.now()); mapper.insert(user);
        saveRoles(user.getId(),request.getRoleIds()); return Result.success();
    }

    @PutMapping("/{id}") @Transactional @RequirePermission("system:user:edit")
    public Result<Void> update(@PathVariable Long id,@Valid @RequestBody UserRequest request){
        SysUser old=mapper.selectById(id); if(old==null||old.getDeleted()==1)throw new BusinessException(404,"用户不存在"); validateUnique(request,id);
        SysUser user=toEntity(request); user.setId(id); user.setPassword(null); user.setUpdateTime(LocalDateTime.now()); mapper.updateById(user); saveRoles(id,request.getRoleIds()); return Result.success();
    }

    @DeleteMapping("/{id}") @Transactional @RequirePermission("system:user:delete")
    public Result<Void> delete(@PathVariable Long id){
        SysUser user=mapper.selectById(id); if(user==null)throw new BusinessException(404,"用户不存在"); if("admin".equals(user.getUsername()))throw new BusinessException("管理员账号不能删除");
        user.setDeleted(1); user.setUpdateTime(LocalDateTime.now()); mapper.updateById(user); userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,id)); return Result.success();
    }

    @PutMapping("/{id}/password") @RequirePermission("system:user:edit") public Result<Void> password(@PathVariable Long id,@Valid @RequestBody PasswordRequest request){
        SysUser user=mapper.selectById(id); if(user==null)throw new BusinessException(404,"用户不存在"); user.setPassword(passwordEncoder.encode(request.getPassword())); user.setUpdateTime(LocalDateTime.now()); mapper.updateById(user); return Result.success();
    }

    @GetMapping("/{id}/roles") @RequirePermission("system:user:list") public Result<List<Long>> roles(@PathVariable Long id){
        return Result.success(userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,id)).stream().map(SysUserRole::getRoleId).toList());
    }
    @PutMapping("/{id}/roles") @Transactional @RequirePermission("system:user:edit") public Result<Void> roles(@PathVariable Long id,@RequestBody IdListRequest request){ saveRoles(id,request.getRoleIds()); return Result.success(); }

    private SysUser toEntity(UserRequest request){
        SysUser user=new SysUser(); user.setUsername(request.getUsername().trim()); user.setNickname(request.getNickname().trim()); user.setPhone(request.getPhone()); user.setEmail(request.getEmail()); user.setStatus(request.getStatus()==null?1:request.getStatus()); return user;
    }
    private void validateUnique(UserRequest request,Long id){
        LambdaQueryWrapper<SysUser> q1=new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername,request.getUsername()).eq(SysUser::getDeleted,0); if(id!=null)q1.ne(SysUser::getId,id); if(mapper.selectCount(q1)>0)throw new BusinessException("账号已存在");
        LambdaQueryWrapper<SysUser> q2=new LambdaQueryWrapper<SysUser>().eq(SysUser::getNickname,request.getNickname()).eq(SysUser::getDeleted,0); if(id!=null)q2.ne(SysUser::getId,id); if(mapper.selectCount(q2)>0)throw new BusinessException("用户名已存在");
    }
    private void saveRoles(Long userId,List<Long> roleIds){
        if(mapper.selectById(userId)==null)throw new BusinessException(404,"用户不存在"); userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,userId));
        for(Long roleId:roleIds){ if(roleMapper.selectById(roleId)==null)continue; SysUserRole x=new SysUserRole(); x.setUserId(userId); x.setRoleId(roleId); userRoleMapper.insert(x); }
    }
    private Map<String,Object> view(SysUser user){
        Map<String,Object> v=new LinkedHashMap<>(); v.put("id",user.getId()); v.put("username",user.getUsername()); v.put("nickname",user.getNickname()); v.put("phone",user.getPhone()); v.put("email",user.getEmail()); v.put("status",user.getStatus()); v.put("createTime",user.getCreateTime());
        List<SysRole> roles=roleMapper.selectByUserId(user.getId()); v.put("roleIds",roles.stream().map(SysRole::getId).toList()); v.put("roleNames",roles.stream().map(SysRole::getRoleName).toList()); return v;
    }
}
