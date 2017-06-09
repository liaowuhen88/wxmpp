package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.Ofproperty;

public interface OfpropertyMapper {
    int deleteByPrimaryKey(String name);

    int insert(Ofproperty record);

    int insertSelective(Ofproperty record);

    Ofproperty selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(Ofproperty record);

    int updateByPrimaryKeyWithBLOBs(Ofproperty record);
}