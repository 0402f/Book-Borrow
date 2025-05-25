package com.slt.slt.controller;

import com.slt.slt.Result.Result;
import com.slt.slt.Result.ResultPage;
import com.slt.slt.domain.DTO.UserLoginDTO;
import com.slt.slt.domain.POJO.User;
import com.slt.slt.service.UserService;
import com.slt.slt.utils.JwtUtil;
import com.slt.slt.utils.ThreadLocalUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.lang.Thread.sleep;


@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     *  用户登录
     * @param
     * @return
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录{}", userLoginDTO);
        User user = userService.login(userLoginDTO);

        // 判断用户是否存在
        if (user == null || user.getUserNumber() == null) {
            return Result.error("用户不存在");
        }

        // 验证密码
        if (!user.getPassword().equals(userLoginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        // 生成JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("userNumber", user.getUserNumber());
        String token = JwtUtil.genToken(claims);

        // 将JWT存储到Redis，使用用户的唯一标识作为键
        String redisKey = "token:" + user.getUserNumber(); // 使用用户编号作为键
        ValueOperations<String, String> op = stringRedisTemplate.opsForValue();
        op.set(redisKey, token, 60, TimeUnit.MINUTES); // 60分钟过期

        log.info("token:{}", token);
        return Result.success(token);
    }

    /**
     *  用户注册
     * @param UserLoginDTO
     * @return
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserLoginDTO UserLoginDTO){
        log.info("用户注册{}",UserLoginDTO);
        // 判断当前账号是否被占用
        User user = userService.findByUserNumber(UserLoginDTO.getUserNumber());
        if(user != null){
            return Result.error("账号已被占用");
        }

        userService.register(UserLoginDTO);
        return Result.success("注册成功");
    }

    /**
     * 获取当前登录用户的信息
     * @return
     */
    @GetMapping("/userInfo")
    public Result<User> findByUserNumber()  {
        Map<String,Object> map = ThreadLocalUtil.get();
        // 根据id来查询用户信息
        Integer id = (Integer) map.get("id");
        log.info("获取当前登录用户的信息,id:{}",id);
        User user = userService.findById(id.toString());
        return Result.success(user);
    }

    /**
     * 用户信息分页查询
     * @param pageNum    页码
     * @param pageSize   每页显示的记录数
     * @return
     */
    @GetMapping("/list")
    public Result<ResultPage<User>> list(Integer pageNum,Integer pageSize) throws InterruptedException {
        log.info("用户信息分页查询,页码:{},每页显示的记录数:{}",pageNum,pageSize);
        // 模拟延时
        sleep(500);
        ResultPage<User> resultPage = userService.list(pageNum,pageSize);
        return Result.success(resultPage);
    }

    /**
     * 删除单个用户
     */
    @DeleteMapping
    public Result<String> delete(Integer id){
        log.info("删除用户,id:{}",id);
        userService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 根据id查询用户信息
     * @param UserId
     * @return
     */
    @GetMapping
    public Result<User> UserInfoById(Integer UserId){
        log.info("根据id查询用户信息,id:{}",UserId);
        User user = userService.findById(UserId.toString());
        return Result.success(user);
    }

    /**
     * 获取用户数量
     * @return
     */
    @GetMapping("/Count")
    public Result<Long> GetUserCount() {
        return Result.success(userService.GetUserCount());
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result<String> Logout(){
        Map<String,Object> map = ThreadLocalUtil.get();
        String userNumber = (String) map.get("userNumber");
        String redisKey = "token:" + userNumber;
        stringRedisTemplate.delete(redisKey);
        return Result.success("退出成功");
    }

}
