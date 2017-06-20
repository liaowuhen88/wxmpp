package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.FromToAdapt;

public interface FromToAdaptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FromToAdapt record);

    int insertSelective(FromToAdapt record);

    FromToAdapt selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FromToAdapt record);

    int updateByPrimaryKey(FromToAdapt record);
}