package com.zghky.waterCollection.controller;

import com.github.pagehelper.PageInfo;
import com.zghky.waterCollection.config.MyConstants;
import com.zghky.waterCollection.domain.CommonResult;
import com.zghky.waterCollection.domain.SysRole;
import com.zghky.waterCollection.domain.SysUser;
import com.zghky.waterCollection.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ModelAttribute
    public void selectById(@RequestParam(value = "id", required = false) Integer id,
                           Map<String, Object> map, HttpServletRequest request) {
        if (id != null) {
            Map<String, Object> objectMap = sysUserService.selectById(id);
            SysUser sysUser = (SysUser) objectMap.get("user");
            map.put("sysUser", sysUser);
            List<SysRole> roles = (List<SysRole>) objectMap.get("roles");
            List<String> roleIdList = new ArrayList<>();
            if(roles.size() > 0) {
                for (SysRole role : roles) {
                    roleIdList.add(role.getId().toString());
                }
            }
            request.setAttribute("roleIds", String.join(";", roleIdList));
        }
    }

    @PostMapping("/deleteSysUserById")
    public CommonResult<SysUser> deleteById(@RequestParam(value = "id", required = true) Integer id) {
        CommonResult<SysUser> commonResult = null;
        try {
            sysUserService.deleteById(id);
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/insertSysUser")
    public CommonResult<SysUser> insert(SysUser sysUser) {
        CommonResult<SysUser> commonResult = null;
        try {
            sysUserService.insert(sysUser);
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "新增用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/selectSysUserById")
    public CommonResult<Map<String, Object>> selectById(@RequestParam(value = "id", required = true) Integer id) {
        CommonResult<Map<String, Object>> commonResult = null;
        try {
            Map<String, Object> map = sysUserService.selectById(id);
            commonResult = new CommonResult(MyConstants.SUCCESS_CODE, "查询成功", map);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/selectAllSysUser")
    public CommonResult<PageInfo<SysUser>> selectAll(@RequestParam Map<String, Object> map) {
        CommonResult<PageInfo<SysUser>> commonResult = null;
        try {
            PageInfo<SysUser> pageInfo = sysUserService.selectAll(map);
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }

    @PostMapping("/updateSysUserById")
    public CommonResult<SysUser> updateById(@ModelAttribute("sysUser") SysUser sysUser, HttpServletRequest request) {
        CommonResult<SysUser> commonResult = null;
        try {
            String roleIds = (String) request.getAttribute("roleIds");
            sysUserService.updateById(sysUser, roleIds);
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, MyConstants.SYSTEM_ERROR_MESSAGE);
        }
        return commonResult;
    }
}
