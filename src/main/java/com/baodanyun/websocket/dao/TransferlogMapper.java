package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.Transferlog;

public interface TransferlogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Transferlog record);

    int insertSelective(Transferlog record);

    Transferlog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Transferlog record);

    int updateByPrimaryKeyWithBLOBs(Transferlog record);

    int updateByPrimaryKey(Transferlog record);
}