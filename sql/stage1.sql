-- 第一阶段幂等补充脚本：按钮权限、索引与初始化数据。
-- 可重复执行，不会覆盖已有业务数据。

CREATE UNIQUE INDEX IF NOT EXISTS uk_sys_menu_path_active ON sys_menu(path) WHERE path IS NOT NULL AND path <> '' AND deleted = 0;
CREATE UNIQUE INDEX IF NOT EXISTS uk_sys_menu_component_active ON sys_menu(component) WHERE component IS NOT NULL AND component <> '' AND deleted = 0;
CREATE UNIQUE INDEX IF NOT EXISTS uk_sys_menu_permission_active ON sys_menu(permission_code) WHERE permission_code IS NOT NULL AND permission_code <> '' AND deleted = 0;

INSERT INTO sys_menu(parent_id,menu_name,menu_type,permission_code,sort_order,visible,status,deleted)
SELECT p.id,v.name,'BUTTON',v.code,v.sort_order,0,1,0
FROM (VALUES
 ('用户管理','新增用户','system:user:add',101),('用户管理','编辑用户','system:user:edit',102),('用户管理','删除用户','system:user:delete',103),
 ('角色管理','新增角色','system:role:add',201),('角色管理','编辑角色','system:role:edit',202),('角色管理','删除角色','system:role:delete',203),
 ('菜单管理','新增菜单','system:menu:add',301),('菜单管理','编辑菜单','system:menu:edit',302),('菜单管理','删除菜单','system:menu:delete',303),
 ('系统配置','修改配置','system:config:edit',401)
) AS v(parent_name,name,code,sort_order)
JOIN sys_menu p ON p.menu_name=v.parent_name AND p.deleted=0
WHERE NOT EXISTS(SELECT 1 FROM sys_menu m WHERE m.permission_code=v.code AND m.deleted=0);

INSERT INTO sys_role_menu(role_id,menu_id)
SELECT r.id,m.id FROM sys_role r CROSS JOIN sys_menu m
WHERE r.role_code='admin' AND r.deleted=0 AND m.deleted=0
AND NOT EXISTS(SELECT 1 FROM sys_role_menu rm WHERE rm.role_id=r.id AND rm.menu_id=m.id);
