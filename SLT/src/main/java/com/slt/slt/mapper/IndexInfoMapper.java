package com.slt.slt.mapper;


import com.slt.slt.domain.POJO.image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndexInfoMapper {

    @Select("select * from image")
    List<image> indexInfo();
}
