package com.li.jwt.service;

import com.li.jwt.domain.SysMenu;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysMenuService {

    List<SysMenu> listWithTree(HttpServletRequest request);
}
