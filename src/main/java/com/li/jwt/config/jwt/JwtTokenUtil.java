package com.li.jwt.config.jwt;

import com.li.jwt.config.auth.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenUtil {

    private String secret;
    private Long expiration;
    private String header;

    /**
     * 获取Token令牌
     *
     * @param myUserDetails 封装了用户信息
     * @return Token令牌
     */
    public String generateToken(MyUserDetails myUserDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", myUserDetails.getUsername());
        claims.put("created", new Date());
        claims.put("userId", myUserDetails.getUserId());

        return generateToken(claims);
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 判断令牌是否过期
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    /**
     * 从令牌中获取用户Id
     * @param token
     * @return
     */
    public Integer getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return (Integer) claims.get("userId");
        }
        return null;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 判断令牌是否过期
     * @returns 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            return claims.getExpiration().before(new Date());
        }
        return false;
    }

    /**
     * 刷新令牌
     *
     * @param token 原Token令牌
     * @return 新Token令牌
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            claims.put("created", new Date());
            return generateToken(claims);
        }
        return null;
    }

    /**
     * 验证Token
     * @param token Token令牌
     * @param userDetails 用户userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return userDetails.getUsername().equals(username) && !isTokenExpired(token);
    }

    /**
     * 从claims生成令牌
     *
     * @param claims 数据声明:用户名和创建时间
     * @return 生成Token令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }
}
