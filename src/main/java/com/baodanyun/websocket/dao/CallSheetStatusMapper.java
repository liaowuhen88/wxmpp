package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.CallSheetStatus;

public interface CallSheetStatusMapper {
    int deleteByPrimaryKey(String callSheetId);

    int insert(CallSheetStatus record);

    int insertSelective(CallSheetStatus record);

    CallSheetStatus selectByPrimaryKey(String callSheetId);

    int updateByPrimaryKeySelective(CallSheetStatus record);

    int updateByPrimaryKey(CallSheetStatus record);
}