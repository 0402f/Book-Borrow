package com.slt.slt.service.serviceImpl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.slt.slt.Result.ResultPage;
import com.slt.slt.mapper.UserMapper;
import com.slt.slt.domain.DTO.UserLoginDTO;
import com.slt.slt.domain.POJO.User;
import com.slt.slt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user =userMapper.login(userLoginDTO);
        //打印user信息
        //System.out.println(user.toString());
        return user;
    }

    /**
     * 用户注册
     * @param userLoginDTO
     */
    @Override
    public void register(UserLoginDTO userLoginDTO) {
        // 将用户密码和用户账号存入数据库
        userMapper.register(userLoginDTO);

    }

    /**
     *  根据用户账号查询用户信息
     * @param userNumber
     * @return
     */
    @Override
    public User findByUserNumber(String userNumber) {
        return userMapper.findByUserNumber(userNumber);
    }

    /**
     *  分页查询用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResultPage<User> list(Integer pageNum, Integer pageSize) {
        // 创建一个resultPage对象
        ResultPage<User> resultPage = new ResultPage<>();
        // 使用 PageHelper 分页
        PageHelper.startPage(pageNum, pageSize);

        // 执行查询，返回 Page<UserVO> 对象
        List<User> userPage = userMapper.list(); // 确保返回类型为 Page<UserVO>
        // 遍历userPage
        for (User user : userPage) {
            // 设置密码为******
            if (user.getPassword() != null) {
                user.setPassword("********");
            }
        }
        // 转为Page对象
        Page<User> page = (Page<User>) userPage;
        // 设置结果
        resultPage.setTotal(page.getTotal());
        resultPage.setItems(page.getResult());
        // 封装结果
        return resultPage;
    }

    @Override
    public User findById(String id) {
        return userMapper.findById(id);
    }

    /**+
     * 更据id删除用户
     * @param id
     */
    @Override
    public void delete(Integer id) {
        userMapper.delete(id);
    }

    @Override
    public Long GetUserCount() {
        return userMapper.GetUserCount();
    }
}
