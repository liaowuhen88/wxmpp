package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;

import java.util.Date;
import java.util.List;

/**
 * Created by liaowuhen on 2017/6/12.
 */
public interface WechatMsgService {
    int insert(WechatMsg record);

    /**
     * 获取当前客服,以天为单位的呼出客户消息数，成功数，失败数
     */

    List<WeChatMsgStatistics> statistics(String jid, Date date) throws BusinessException;

}
