package com.li.jwt.controller;

import com.li.jwt.service.SysMenuService;
import com.li.jwt.config.MyConstants;
import com.li.jwt.domain.CommonResult;
import com.li.jwt.domain.SysMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/listWithTree")
    public CommonResult<List<SysMenu>> listWithTree(HttpServletRequest request) {
        CommonResult<List<SysMenu>> commonResult = null;
        try {
            List<SysMenu> sysMenus = sysMenuService.listWithTree(request);
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "查询成功");
        } catch (Exception e) {
            commonResult = new CommonResult<>(MyConstants.SYSTEM_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }
}
