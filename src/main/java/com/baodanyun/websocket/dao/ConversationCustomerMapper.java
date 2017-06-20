package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.ConversationCustomer;

public interface ConversationCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConversationCustomer record);

    int insertSelective(ConversationCustomer record);

    ConversationCustomer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConversationCustomer record);

    int updateByPrimaryKeyWithBLOBs(ConversationCustomer record);

    int updateByPrimaryKey(ConversationCustomer record);
}