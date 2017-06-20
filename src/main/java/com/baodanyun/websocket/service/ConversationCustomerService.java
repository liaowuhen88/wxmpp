package com.baodanyun.websocket.service;

import com.baodanyun.websocket.model.ConversationCustomer;

import java.util.List;

/**
 * Created by liaowuhen on 2017/6/20.
 */
public interface ConversationCustomerService {
    int insert(ConversationCustomer record);

    int delete(ConversationCustomer record);

    List<ConversationCustomer> select(ConversationCustomer record);

}
