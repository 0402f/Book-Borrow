package com.slt.slt.service.serviceImpl;

import com.slt.slt.mapper.IndexInfoMapper;
import com.slt.slt.domain.POJO.image;
import com.slt.slt.service.IndexInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class IndexInfoServiceImpl implements IndexInfoService {
    @Autowired
    private IndexInfoMapper indexInfoMapper;

    @Override
    public List<image> indexInfo() {

        return indexInfoMapper.indexInfo();
    }


}
