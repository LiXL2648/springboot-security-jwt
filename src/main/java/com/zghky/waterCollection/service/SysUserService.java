package com.zghky.waterCollection.service;

import com.github.pagehelper.PageInfo;
import com.zghky.waterCollection.domain.SysUser;

import java.util.Map;

public interface SysUserService {

    int deleteById(Integer id);

    int insert(SysUser sysUser);

    Map<String, Object> selectById(Integer id);

    PageInfo<SysUser> selectAll(Map<String, Object> map);

    int updateById(SysUser sysUser, String roleIds);
}
