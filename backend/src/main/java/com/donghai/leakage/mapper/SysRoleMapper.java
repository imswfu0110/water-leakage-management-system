package com.donghai.leakage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.donghai.leakage.entity.SysRole;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    @Select("SELECT r.* FROM sys_role r JOIN sys_user_role ur ON ur.role_id=r.id WHERE ur.user_id=#{userId} AND r.deleted=0 AND r.status=1 ORDER BY r.id")
    List<SysRole> selectByUserId(Long userId);
}
