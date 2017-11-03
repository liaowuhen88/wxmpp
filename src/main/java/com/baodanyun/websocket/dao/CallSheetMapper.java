package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.CallSheet;

public interface CallSheetMapper {
    int deleteByPrimaryKey(String id);

    int insert(CallSheet record);

    int insertSelective(CallSheet record);

    CallSheet selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CallSheet record);

    int updateByPrimaryKey(CallSheet record);
}