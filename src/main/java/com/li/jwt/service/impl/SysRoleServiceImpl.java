package com.li.jwt.service.impl;

import com.li.jwt.mapper.SysRoleMapper;
import com.li.jwt.service.SysRoleService;
import com.li.jwt.domain.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> selectAll() {
        return sysRoleMapper.selectAll();
    }
}
