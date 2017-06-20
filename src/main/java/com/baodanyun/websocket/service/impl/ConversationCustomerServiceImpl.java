package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.dao.ConversationCustomerMapper;
import com.baodanyun.websocket.model.ConversationCustomer;
import com.baodanyun.websocket.service.ConversationCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liaowuhen on 2017/6/20.
 */
@Service
public class ConversationCustomerServiceImpl implements ConversationCustomerService {

    @Autowired
    private ConversationCustomerMapper conversationCustomerMapper;

    @Override
    public int insert(ConversationCustomer record) {
        List<ConversationCustomer> li = conversationCustomerMapper.select(record);
        if (null != li && li.size() > 0) {
            return 1;
        }
        return conversationCustomerMapper.insert(record);
    }

    @Override
    public int delete(ConversationCustomer record) {
        return conversationCustomerMapper.delete(record);
    }

    @Override
    public List<ConversationCustomer> select(ConversationCustomer record) {
        return conversationCustomerMapper.select(record);
    }


}
