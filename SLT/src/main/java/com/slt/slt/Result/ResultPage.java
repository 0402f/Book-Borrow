package com.slt.slt.Result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一返回结果类
 * 用来封装返回给前端的数据--分页查询
 */

@Data // lombok注解，自动生成getter和setter方法
@NoArgsConstructor // lombok注解，自动生成无参构造方法
@AllArgsConstructor // lombok注解，自动生成全参构造方法
public class ResultPage<T> {
    private Long total; //总记录数
    private List<T> items; //数据列表


}
