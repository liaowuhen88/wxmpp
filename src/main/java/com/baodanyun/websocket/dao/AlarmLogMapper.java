package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.AlarmLog;
import com.baodanyun.websocket.model.AlarmLogExample;

public interface AlarmLogMapper {
    int countByExample(AlarmLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlarmLog record);

    int insertSelective(AlarmLog record);

    AlarmLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmLog record);

    int updateByPrimaryKey(AlarmLog record);
}