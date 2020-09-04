package com.li.jwt.mapper;

import com.li.jwt.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper {
    int deleteById(Integer id);

    int insert(SysUser sysUser);

    SysUser selectById(Integer id);

    List<SysUser> selectAll(@Param("searchText") String searchText);

    int updateById(SysUser sysUser);

    void inserUserRole(@Param("userId") Integer userId, @Param("roleIdList") List<Integer> roleIdList);

    void deleteUserRole(@Param("userId") Integer userId, @Param("roleIdList") List<Integer> roleIdList);
}