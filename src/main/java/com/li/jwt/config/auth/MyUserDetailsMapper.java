package com.li.jwt.config.auth;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyUserDetailsMapper {

    // 根据用户名查询用户信息
    MyUserDetails findByUserName(String username);

    // 根据用户名查询用户角色
    List<String> findRoleByUserName(String username);

    // 根据用户角色查询用户权限
    List<String> findAuthorityByRoleCodes(@Param("roles") List<String> roles);
}
