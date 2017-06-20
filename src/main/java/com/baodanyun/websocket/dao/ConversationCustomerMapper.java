package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.ConversationCustomer;

import java.util.List;

public interface ConversationCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int delete(ConversationCustomer record);

    List<ConversationCustomer> select(ConversationCustomer record);

    int insert(ConversationCustomer record);

    int insertSelective(ConversationCustomer record);

    ConversationCustomer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConversationCustomer record);

    int updateByPrimaryKeyWithBLOBs(ConversationCustomer record);

    int updateByPrimaryKey(ConversationCustomer record);
}