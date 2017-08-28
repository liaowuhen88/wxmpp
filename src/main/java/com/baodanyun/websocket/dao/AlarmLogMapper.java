package com.baodanyun.websocket.dao;

import com.baodanyun.robot.dto.AlarmStatisticsDto;
import com.baodanyun.websocket.model.AlarmLog;
import com.baodanyun.websocket.model.AlarmLogExample;

import java.util.List;

public interface AlarmLogMapper {
    int countByExample(AlarmLogExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AlarmLog record);

    int insertSelective(AlarmLog record);

    AlarmLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AlarmLog record);

    int updateByPrimaryKey(AlarmLog record);

    /**
     * 统计5分钟15分钟30分钟的客服告警次数
     *
     * @param alarmStatisticsDto
     * @return
     */
    List<AlarmStatisticsDto> statisticsAlarm(AlarmStatisticsDto alarmStatisticsDto);
}