package com.slt.slt.config;

import com.slt.slt.interceptors.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("进入跨域配置");
        registry.addMapping("/**") // 允许所有路径
                // .allowedOrigins("http://8.148.70.244:3000", "http://8.148.70.244:3001") // 上线
                .allowedOrigins("http://localhost:3000","http://localhost:3001") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowCredentials(true) // 允许携带凭证
                .allowedHeaders("*"); // 允许的请求头
    }
    // 添加拦截器

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将拦截器添加到拦截器链中
        log.info("进入拦截器配置");
        registry.addInterceptor(loginInterceptor).excludePathPatterns("/user/login","/user/register");

    }
}