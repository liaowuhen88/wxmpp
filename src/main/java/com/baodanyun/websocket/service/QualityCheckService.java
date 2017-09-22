package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;
import java.util.Map;

public interface QualityCheckService {

    /**
     * 查询当前客服聊天的所有用户
     *
     * @param qualitySearchDto
     * @return
     */
    List<String> findAllGuestName(QualitySearchDto qualitySearchDto);

    /**
     * 查询客服与此用户的日期区间内的聊天记录
     *
     * @param searchDto
     * @return
     */
    List<HistoryMsg> loadChatMsgFromUser(QualitySearchDto searchDto);

    /**
     * 根据用户电话查询用户信息
     *
     * @param phoneList
     * @return
     */
    List<PersonalInfo> findUserList(List<String> phoneList);

    /**
     * 从mongodb统计事件总数
     *
     * @param evtCode   事件code
     * @param searchDto 查询条件
     * @return
     */
    AggregationResults<Map> getEventResult(String evtCode, QualitySearchDto searchDto);

    Map<String, Integer> getEventTotalMap(QualitySearchDto searchDto);

    List<PersonalInfo> findMongoEvtData(int code, QualitySearchDto searchDto);
}
