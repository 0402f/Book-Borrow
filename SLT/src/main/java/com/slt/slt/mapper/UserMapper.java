package com.slt.slt.mapper;

import com.slt.slt.domain.DTO.UserLoginDTO;
import com.slt.slt.domain.POJO.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    /**
     *  查询用户信息
     * @param userLoginDTO
     * @return
     */
    @Select("select * from users where user_number = #{userNumber}")
    User login(UserLoginDTO userLoginDTO);

    /**
     * 用户注册
     * @param userLoginDTO
     */
    @Insert("Insert into users(user_number,password) values(#{userNumber},#{password})")
    void register(UserLoginDTO userLoginDTO);

    /**
     * 根据用户账号查询用户信息
     * @param userNumber
     * @return
     */
    @Select("select * from users where user_number = #{userNumber}")
    User findByUserNumber(String userNumber);

    /**
     *  分页查询用户信息
     * @return
     */
    List<User> list();

    /**
     *  根据id查询用户信息
     * @param id
     * @return
     */
    @Select("select * from users where id = #{id}")
    User findById(String id);

    /**
     *  更据id删除用户
     * @param id
     */
    @Delete("delete from users where id = #{id}")
    void delete(Integer id);

    /**
     *  获取用户数量
     * @return
     */
    @Select("select count(*) from users")
    Long GetUserCount();
}
