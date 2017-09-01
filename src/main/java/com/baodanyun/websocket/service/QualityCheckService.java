package com.baodanyun.websocket.service;

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
}
