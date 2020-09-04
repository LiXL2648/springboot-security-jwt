package com.li.jwt.mapper;

import com.li.jwt.domain.SysMenu;
import com.li.jwt.domain.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    int deleteById(Integer id);

    int insert(SysMenu sysMenu);

    SysMenu selectById(Integer id);

    List<SysMenu> selectAll(Integer userId);

    int updateById(SysMenu record);

    List<SysMenu> selectByRoles(@Param("roles") List<SysRole> roles);
}