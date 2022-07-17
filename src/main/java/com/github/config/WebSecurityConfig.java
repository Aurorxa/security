package com.github.config;

import cn.hutool.core.map.MapUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.filter.JwtFilter;
import com.github.filter.RestAuthenticationFilter;
import com.github.provider.MobileAuthenticationFilter;
import com.github.provider.MobileAuthenticationProvider;
import com.github.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

/**
 * @author 许大仙
 * @version 1.0
 * @since 2022-07-14 10:31:22
 */
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @NonNull
    private ObjectMapper objectMapper;
    @NonNull
    private UserDetailsService userDetailsService;
    @NonNull
    private UserService userService;
    @NonNull
    private UserDetailsPasswordService userDetailsPasswordService;
    @NonNull
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestAuthenticationFilter requestAuthenticationFilter() throws Exception {
        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter(objectMapper);
        restAuthenticationFilter.setAuthenticationManager(authenticationManager());
        restAuthenticationFilter.setFilterProcessesUrl("/authorize/login");
        restAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        restAuthenticationFilter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        return restAuthenticationFilter;
    }

    private AuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
        return (req, res, auth) -> {
            res.setStatus(HttpStatus.OK.value());
            res.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(auth));
            log.info("认证成功");
        };
    }

    private AuthenticationFailureHandler jsonLoginFailureHandler() {
        return (req, res, exp) -> {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
            res.setCharacterEncoding("UTF-8");

            HashMap<Object, Object> errData = MapUtil.of(new String[][]{
                    {"title", "认证失败"},
                    {"details", exp.getMessage()}
            });

            res.getWriter().println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errData));
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
        auth.authenticationProvider(phoneAuthenticationProvider());
    }


    /**
     * 配置数据库 Provider
     *
     * @return
     */
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsPasswordService(userDetailsPasswordService);
        return daoAuthenticationProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * 增加手机号 Provider
     *
     * @return
     */
    public MobileAuthenticationProvider phoneAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        mobileAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        mobileAuthenticationProvider.setUserService(userService);
        return mobileAuthenticationProvider;
    }


    @Bean
    public MobileAuthenticationFilter phoneAuthenticationFilter() throws Exception {
        MobileAuthenticationFilter mobileAuthenticationFilter = new MobileAuthenticationFilter(objectMapper);
        mobileAuthenticationFilter.setAuthenticationManager(authenticationManager());
        mobileAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        mobileAuthenticationFilter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
        return mobileAuthenticationFilter;
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // .requestMatchers(req -> req.mvcMatchers("/api/**", "/admin/**", "/authorize/**"))
                // 前后端分离模式不需要 session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(requestAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(phoneAuthenticationFilter(), RestAuthenticationFilter.class)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/authorize/**").permitAll()
                        .antMatchers("/login").permitAll()
                        .antMatchers("/register").permitAll()
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/api/**").hasRole("USER")
                        .antMatchers("/token/**").permitAll()
                        .anyRequest().authenticated())
                // 禁止表单登录
                .formLogin(AbstractHttpConfigurer::disable)
                // 禁止 basic 登录
                .httpBasic(AbstractHttpConfigurer::disable)
                // 禁止 csrf
                .csrf(AbstractHttpConfigurer::disable)
                // 禁止默认退出
                .logout(AbstractHttpConfigurer::disable);

    }

}
