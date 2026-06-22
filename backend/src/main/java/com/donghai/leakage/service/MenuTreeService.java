package com.donghai.leakage.service;

import com.donghai.leakage.entity.SysMenu;
import com.donghai.leakage.mapper.SysMenuMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MenuTreeService {
    private final SysMenuMapper mapper;
    public MenuTreeService(SysMenuMapper mapper) { this.mapper = mapper; }

    public List<SysMenu> allTree() {
        return tree(mapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSortOrder, SysMenu::getId)));
    }

    public List<SysMenu> userTree(Long userId) {
        return tree(mapper.selectByUserId(userId).stream().filter(m -> m.getVisible() == 1).toList());
    }

    public List<SysMenu> tree(List<SysMenu> menus) {
        Map<Long, SysMenu> byId = new LinkedHashMap<>();
        menus.forEach(m -> { m.setChildren(new ArrayList<>()); byId.put(m.getId(), m); });
        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu menu : menus) {
            SysMenu parent = byId.get(menu.getParentId());
            if (parent == null || menu.getParentId() == 0) roots.add(menu); else parent.getChildren().add(menu);
        }
        return roots;
    }
}
