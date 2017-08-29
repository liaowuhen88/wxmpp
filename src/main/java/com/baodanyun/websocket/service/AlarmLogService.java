package com.baodanyun.websocket.service;

import com.baodanyun.robot.dto.AlarmStatisticsDto;

import java.util.List;

public interface AlarmLogService {

    List<AlarmStatisticsDto> statisticsAlarm(AlarmStatisticsDto alarmStatisticsDto);
}
