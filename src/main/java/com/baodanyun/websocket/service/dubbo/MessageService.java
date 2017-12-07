package com.baodanyun.websocket.service.dubbo;

import com.baodanyun.websocket.bean.QuickReply;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.dubbo.bean.Message;

import java.util.List;

/**
 * Created by liaowuhen on 2017/12/1.
 */
public interface MessageService {
    List<Message> getByMessage(Message query) throws BusinessException;

    List<Message> getByMessage(Long uid, Integer type) throws BusinessException;

    List<QuickReply> MessageToQuickReply(List<Message> list);
}
