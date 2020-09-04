package com.li.jwt.mapper;

import com.li.jwt.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleMapper {
    int deleteById(Integer id);

    int insert(SysRole sysRole);

    SysRole selectById(Integer id);

    List<SysRole> selectAll();

    int updateById(SysRole sysRole);

    List<SysRole> selectByUserId(Integer userId);
}