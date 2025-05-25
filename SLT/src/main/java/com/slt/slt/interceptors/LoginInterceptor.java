package com.slt.slt.interceptors;

import com.slt.slt.utils.JwtUtil;
import com.slt.slt.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * 登录拦截器
 *
 * @author 123
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * redis
     */
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 处理请求前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // System.out.println("当前线程的id:" + Thread.currentThread().getId());
        // log.info("当前线程的id:{}", Thread.currentThread().getId());
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        // 1.令牌验证
        String token = request.getHeader("JWT"); // 获取请求头中的令牌
        // System.out.println("token:" + token);
        // log.info("token:{}", token);
        // 2.从redis中获取用户信息
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            String userNumber = (String) claims.get("userNumber");
            if (userNumber == null) {
                throw new RuntimeException("Invalid token");
            }
            String redisKey = "token:" + userNumber;

            ValueOperations<String, String> op = stringRedisTemplate.opsForValue();
            String redisToken = op.get(redisKey);
            if (redisToken == null) {
                // token失效 抛出异常
                throw new RuntimeException();
            }

            if (!redisToken.equals(token)) {
                // token失效 抛出异常
                throw new RuntimeException();
            }

            // 把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);

            // 如果解析成功 则放行
            return true;
        } catch (RuntimeException e) {
            // 解析失败 抛出异常
            response.setStatus(401); // 设置响应状态码为401
            return false; // 不放行
        }

    }

    /**
     * 处理请求后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 移除用户信息 避免内存泄露
        ThreadLocalUtil.remove();
    }
}
