package com.zghky.waterCollection.controller;

import com.zghky.waterCollection.config.MyConstants;
import com.zghky.waterCollection.domain.CommonResult;
import com.zghky.waterCollection.domain.SysRole;
import com.zghky.waterCollection.service.SysRoleService;
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
