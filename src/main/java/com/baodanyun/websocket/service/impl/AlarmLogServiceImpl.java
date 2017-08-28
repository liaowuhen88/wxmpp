package com.baodanyun.websocket.service.impl;

import com.baodanyun.robot.dto.AlarmStatisticsDto;
import com.baodanyun.websocket.dao.AlarmLogMapper;
import com.baodanyun.websocket.service.AlarmLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmLogServiceImpl implements AlarmLogService {

    @Autowired
    private AlarmLogMapper alarmLogMapper;

    @Override
    public List<AlarmStatisticsDto> statisticsAlarm(AlarmStatisticsDto alarmStatisticsDto) {
        return alarmLogMapper.statisticsAlarm(alarmStatisticsDto);
    }
}
