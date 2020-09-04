package com.zghky.waterCollection.service;

import com.zghky.waterCollection.domain.SysMenu;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysMenuService {

    List<SysMenu> listWithTree(HttpServletRequest request);
}
