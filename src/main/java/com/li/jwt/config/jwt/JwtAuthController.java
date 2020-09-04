package com.li.jwt.config.jwt;

import com.li.jwt.config.MyConstants;
import com.li.jwt.domain.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class JwtAuthController {

    @Autowired
    private JwtAuthService jwtAuthService;

    @PostMapping("/login")
    public CommonResult<String> login(@RequestBody Map<String, String> map, HttpServletRequest request) {
        CommonResult<String> commonResult = new CommonResult<>();
        String username = map.get("username");
        String password = map.get("password");

        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()) {
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, "账号或者密码不能为空");
        } else {
            try {
                String token = jwtAuthService.login(username, password);
                commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "登录成功", token);
            } catch (Exception e) {
                commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, "账号或者密码错误");
            }
        }
        return commonResult;
    }

    @PostMapping("/refreshToken")
    public CommonResult<String> refreshToken(@RequestHeader("${jwt.header}") String token) {
        String newToken = jwtAuthService.refreshToken(token);
        CommonResult<String> commonResult = null;
        if (newToken != null) {
            commonResult = new CommonResult<>(MyConstants.SUCCESS_CODE, "令牌刷新", newToken);
        } else {
            commonResult = new CommonResult<>(MyConstants.OPERATEDB_ERROR_CODE, "令牌已过期，请重新登录");
        }
        return commonResult;
    }
}
