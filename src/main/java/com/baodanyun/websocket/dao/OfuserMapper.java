package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.Ofuser;

public interface OfuserMapper {
    int deleteByPrimaryKey(String username);

    int insert(Ofuser record);

    int insertSelective(Ofuser record);

    Ofuser selectByPrimaryKey(String username);

    int updateByPrimaryKeySelective(Ofuser record);

    int updateByPrimaryKey(Ofuser record);
}