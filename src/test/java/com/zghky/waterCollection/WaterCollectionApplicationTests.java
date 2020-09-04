package com.zghky.waterCollection;

import com.zghky.waterCollection.config.auth.MyUserDetails;
import com.zghky.waterCollection.config.auth.MyUserDetailsMapper;
import com.zghky.waterCollection.config.auth.MyUserDetailsService;
import com.zghky.waterCollection.config.jwt.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WaterCollectionApplicationTests {

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    public void testLoadUserByUsername() {
        System.out.println(myUserDetailsService.loadUserByUsername("admin").getAuthorities());
    }

    @Test
    public void testMyUserDetailsMapper() {
        MyUserDetails myUserDetails = myUserDetailsMapper.findByUserName("admin");
        System.out.println(myUserDetails.getUsername());
        List<String> roles = myUserDetailsMapper.findRoleByUserName("admin");
        System.out.println(roles);
        List<String> menus = myUserDetailsMapper.findAuthorityByRoleCodes(roles);
        System.out.println(menus);
    }

    @Test
    public void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    public void testJwtTokenUtil() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImNyZWF0ZWQiOjE1OTg1OTY4NzkwNTcsImV4cCI6MTU5ODYwMDQ3OSwidXNlcklkIjoxfQ.9HnJCEQkBEKUIUfDNtZlQHw8tI7sJ_rvvdFOtaz8cKjCyYkGY9fNALq5-BuSpH56UIwFgvXrEcgwJM7Wps8WnA";
        Integer userId = jwtTokenUtil.getUserIdFromToken(token);
        System.out.println(userId);
    }
}
