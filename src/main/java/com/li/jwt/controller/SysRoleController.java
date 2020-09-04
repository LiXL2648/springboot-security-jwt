package com.li.jwt.controller;

import com.li.jwt.service.SysRoleService;
import com.li.jwt.config.MyConstants;
import com.li.jwt.domain.CommonResult;
import com.li.jwt.domain.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("/selectAllSysRole")
    public CommonResult<List<SysRole>> selectAll() {
        List<SysRole> roles = sysRoleService.selectAll();
        return new CommonResult<List<SysRole>>(MyConstants.SUCCESS_CODE, "查询成功", roles);
    }
}
