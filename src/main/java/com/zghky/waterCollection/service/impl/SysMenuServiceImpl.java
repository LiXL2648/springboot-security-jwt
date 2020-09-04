package com.zghky.waterCollection.service.impl;

import com.zghky.waterCollection.config.jwt.JwtTokenUtil;
import com.zghky.waterCollection.domain.SysMenu;
import com.zghky.waterCollection.mapper.SysMenuMapper;
import com.zghky.waterCollection.service.SysMenuService;
import com.zghky.waterCollection.util.FileUtil;
import com.zghky.waterCollection.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service("SysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public List<SysMenu> listWithTree(HttpServletRequest request) {
        int userId = ObjectUtil.getUserId(request, jwtTokenUtil);
        // 1. 查出所有分类
        List<SysMenu> menus = sysMenuMapper.selectAll(userId);
        // 2. 组装成父子的树形结构

        // 2.1 找到所有一级分类
        List<SysMenu> level1Menus = menus.stream().filter(menu ->
                menu.getMenuPid() == 0
        ).map(menu -> {
            menu.setChildren(getChildren(menu, menus));
            return menu;
        }).sorted((menu1, menu2) -> {
            return menu1.getSort() - menu2.getSort();
        }).collect(Collectors.toList());


        return level1Menus;
    }

    /**
     * 递归查找当前菜单的子菜单
     * @param sysMenu 当前菜单
     * @param menus 所有菜单
     * @return 所有包含了子菜单的菜单
     */
    private List<SysMenu> getChildren(SysMenu sysMenu, List<SysMenu> menus) {
        List<SysMenu> children = menus.stream().filter(menu ->
                menu.getMenuPid() == sysMenu.getId()
        ).map(menu -> {
            // 找到子菜单
            menu.setChildren(getChildren(menu, menus));
            return menu;
        }).sorted((menu1, menu2) -> {
            // 菜单排序
            return menu1.getSort() - menu2.getSort();
        }).collect(Collectors.toList());

        return children;
    }
}
