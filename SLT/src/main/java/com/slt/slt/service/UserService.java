package com.slt.slt.service;

import com.slt.slt.Result.ResultPage;
import com.slt.slt.domain.DTO.UserLoginDTO;
import com.slt.slt.domain.POJO.User;

public interface UserService {
    /**
     *  用户登录
     *  @param userLoginDTO
     */
    User login(UserLoginDTO userLoginDTO);

    /**
     *  用户注册
     * @param userLoginDTO
     */
    void register(UserLoginDTO userLoginDTO);

    /**
     * 根据用户账号查询用户信息
     * @param userNumber
     * @return
     */
    User findByUserNumber(String userNumber);

    /**
     *  分页查询用户信息
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResultPage<User> list(Integer pageNum, Integer pageSize);

    /**
     *  根据id查询用户信息
     * @param toString
     * @return
     */
    User findById(String toString);

    /**
     *  更据id删除用户
     * @param id
     */
    void delete(Integer id);

    /**
     *  获取用户数量
     * @return
     */
    Long GetUserCount();
}
