package com.li.jwt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.li.jwt.mapper.SysMenuMapper;
import com.li.jwt.mapper.SysRoleMapper;
import com.li.jwt.domain.SysMenu;
import com.li.jwt.domain.SysRole;
import com.li.jwt.domain.SysUser;
import com.li.jwt.mapper.SysUserMapper;
import com.li.jwt.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service("SysUserService")
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public int deleteById(Integer id) {
        return sysUserMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int insert(SysUser sysUser) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        Timestamp date = new Timestamp(System.currentTimeMillis());
        sysUser.setCreateTime(date);
        sysUser.setUpdateTime(date);
        int row = sysUserMapper.insert(sysUser);
        String roles = sysUser.getRoles();
        if (!roles.isEmpty()) {
            String[] roleIds = sysUser.getRoles().split(";");
            List<Integer> roleIdList = new ArrayList<>();
            for (String roleId: roleIds) {
                roleIdList.add(Integer.parseInt(roleId));
            }
            sysUserMapper.inserUserRole(sysUser.getId(), roleIdList);
        }
        return row;
    }

    @Override
    public Map<String, Object> selectById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        // 1. 根据Id获取用户信息
        SysUser user = sysUserMapper.selectById(id);
        map.put("user", user);

        // 2. 根据用户获取角色信息
        List<SysRole> roles = sysRoleMapper.selectByUserId(id);
        map.put("roles", roles);

        // 3. 根据用户角色获取用户权限
        if (roles.size() > 0) {
            List<SysMenu> menus = sysMenuMapper.selectByRoles(roles);
            map.put("menus", menus);
        }
        return map;
    }

    @Override
    public PageInfo<SysUser> selectAll(Map<String, Object> map) {
        String searchText = (String) map.get("searchText");
        if (map.get("pageNum") != null && map.get("pageSize") != null) {
            int pageNum = Integer.parseInt((String) map.get("pageNum"));
            int pageSize = Integer.parseInt((String) map.get("pageSize"));
            PageHelper.startPage(pageNum, pageSize);
        }
        List<SysUser> users = sysUserMapper.selectAll(searchText);
        PageInfo<SysUser> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    @Override
    @Transactional
    public int updateById(SysUser sysUser, String roleIds) {
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (!sysUser.getPassword().isEmpty()) {
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        }
        int row = sysUserMapper.updateById(sysUser);

        if (sysUser.getRoles().isEmpty()) {
            if(!roleIds.isEmpty()) {
                // 删除
                List<Integer> roleIdList = getRoleIdList(roleIds);
                sysUserMapper.deleteUserRole(sysUser.getId(), roleIdList);
            }
        } else {
            if (roleIds.isEmpty()) {
                // 新增
                List<Integer> roleIdList = getRoleIdList(sysUser.getRoles());
                sysUserMapper.inserUserRole(sysUser.getId(), roleIdList);
            } else {
                // 替换
                List<Integer> newRoleIdList = getRoleIdList(sysUser.getRoles());
                List<Integer> oldRoleIdList = getRoleIdList(roleIds);
                List<Integer> insertRoleIdList = getDiffList(newRoleIdList, oldRoleIdList);
                List<Integer> deleteRoleIdList = getDiffList(oldRoleIdList, newRoleIdList);
                if (insertRoleIdList.size() > 0) {
                    sysUserMapper.inserUserRole(sysUser.getId(), insertRoleIdList);
                }
                if (deleteRoleIdList.size() > 0) {
                    sysUserMapper.deleteUserRole(sysUser.getId(), deleteRoleIdList);
                }
            }
        }
        return row;
    }

    private List<Integer> getRoleIdList(String roleIds) {
        String[] roleIdStrs = roleIds.split(";");
        List<Integer> roleIdList = new ArrayList<>();
        for (String roleIdStr : roleIdStrs) {
            roleIdList.add(Integer.parseInt(roleIdStr));
        }
        return roleIdList;
    }

    private List<Integer> getDiffList(List<Integer> firstList, List<Integer> lastList) {
        List<Integer> diffList = new ArrayList<>();
        for (int roleId: firstList) {
            if (!lastList.contains(roleId)) {
                // 新增
                diffList.add(roleId);
            }
        }
        return diffList;
    }
}
