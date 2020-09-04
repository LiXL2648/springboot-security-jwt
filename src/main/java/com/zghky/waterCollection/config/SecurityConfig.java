package com.zghky.waterCollection.config;

import com.zghky.waterCollection.config.auth.MyUserDetailsService;
import com.zghky.waterCollection.config.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 设置跨域攻击策略
//                .ignoringAntMatchers("/login") // 忽略登录请求
//                .and()
                .cors() // 设置跨域请求配置
                .and()
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .logout() // 默认退出url为/logout
                .logoutUrl("/logout") // 指定退出的url，前端退出按钮需保持一致
                .deleteCookies("JSESSIONID") // 删除cookie
                .and()
                .rememberMe() // 开启记住我的功能
                .rememberMeParameter("rememberMe") // 设置记住我参数名
                .rememberMeCookieName("REMEMBER-ME-COOKIE") // 记住我的cookie名
                .tokenValiditySeconds(3 * 24 * 60 * 60) // 记住我的有效时长
                .tokenRepository(persistentTokenRepository()) //token数据库存储方式
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/refreshToken", "/showPicture").permitAll() // 不需要通过登录验证就可以被访问的资源路径
                .antMatchers("/fileUpload", "/deleteFile", "/listWithTree").authenticated() // 用户登录即可访问的页面，即不需要任何权限
                .anyRequest().access("@rbacService.hasPermission(request, authentication)") //判断用户是否具备访问资源的权限
                .and()
                .sessionManagement() // // session的固化保护功能
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 用户无状态登录
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST"));
        corsConfiguration.applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
