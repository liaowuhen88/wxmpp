package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.CCProperties;

public interface CCPropertiesMapper {
    int deleteByPrimaryKey(String ccname);

    int insert(CCProperties record);

    int insertSelective(CCProperties record);

    CCProperties selectByPrimaryKey(String ccname);

    int updateByPrimaryKeySelective(CCProperties record);

    int updateByPrimaryKeyWithBLOBs(CCProperties record);
}