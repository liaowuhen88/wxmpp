package com.baodanyun.cc.service.impl;

import com.baodanyun.cc.service.CCPropertiesService;
import com.baodanyun.websocket.dao.CCPropertiesMapper;
import com.baodanyun.websocket.model.CCProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/11/1.
 */
@Service
public class CCPropertiesServiceImpl implements CCPropertiesService {
    private static final Logger logger = LoggerFactory.getLogger(CCServiceImpl.class);

    @Autowired
    private CCPropertiesMapper cCPropertiesMapper;


    @Override
    public int insert(CCProperties record) {
        return cCPropertiesMapper.insert(record);
    }

    @Override
    public CCProperties selectByName(String name) {
        return cCPropertiesMapper.selectByPrimaryKey(name);
    }

    @Override
    public int updateByPrimaryKeySelective(CCProperties record) {
        return cCPropertiesMapper.updateByPrimaryKeySelective(record);
    }


}
