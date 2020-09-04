package com.zghky.waterCollection.config.jwt;

import com.zghky.waterCollection.config.auth.MyUserDetails;
import com.zghky.waterCollection.config.auth.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService MyUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 登录认证换取JWT令牌
     *
     * @param username 用户名
     * @param password 密码
     * @return JWT Token 令牌
     */
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        MyUserDetails myUserDetails = MyUserDetailsService.loadUserByUsername(username);
        return jwtTokenUtil.generateToken(myUserDetails);
    }

    /**
     *  刷新令牌
     * @param token 原无过期令牌
     * @return 新的令牌
     */
    public String refreshToken(String token) {
        if (!jwtTokenUtil.isTokenExpired(token)) {
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }
}
