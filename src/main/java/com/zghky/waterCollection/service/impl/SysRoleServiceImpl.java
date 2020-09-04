package com.zghky.waterCollection.service.impl;

import com.zghky.waterCollection.domain.SysRole;
import com.zghky.waterCollection.mapper.SysRoleMapper;
import com.zghky.waterCollection.service.SysRoleService;
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
