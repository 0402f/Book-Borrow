package com.slt.slt.controller;


import com.slt.slt.Result.Result;
import com.slt.slt.domain.POJO.image;
import com.slt.slt.service.IndexInfoService;
import com.slt.slt.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/index")
public class IndexInfoController {

    @Autowired
    private IndexInfoService indexInfoService;


    @GetMapping
    public Result<List<image>> indexInfo(){
        // 从ThreadLocal获取用户信息
        Map<String, Object> userInfo = ThreadLocalUtil.get();
        if (userInfo != null) {
            System.out.println(userInfo);
        }
        List<image> list = indexInfoService.indexInfo();
        return Result.success(list);


    }
}
