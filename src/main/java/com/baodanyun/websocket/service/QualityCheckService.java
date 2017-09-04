package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;

import java.util.List;

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
}
