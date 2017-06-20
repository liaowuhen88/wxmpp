package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.ConversationCustomerMapper;
import com.baodanyun.websocket.service.ConversationCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/20.
 */
@Service
public class ConversationCustomerServiceImpl implements ConversationCustomerService {

    @Autowired
    private ConversationCustomerMapper conversationCustomerMapper;
}
