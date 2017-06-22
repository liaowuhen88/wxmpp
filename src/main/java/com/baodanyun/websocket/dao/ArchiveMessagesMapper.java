package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.model.ArchiveMessages;
import com.baodanyun.websocket.model.HistoryMessageModel;

import java.util.List;

public interface ArchiveMessagesMapper {
    int deleteByPrimaryKey(Long messageid);

    int insert(ArchiveMessages record);

    int insertSelective(ArchiveMessages record);

    ArchiveMessages selectByPrimaryKey(Long messageid);

    int updateByPrimaryKeySelective(ArchiveMessages record);

    int updateByPrimaryKeyWithBLOBs(ArchiveMessages record);

    int updateByPrimaryKey(ArchiveMessages record);

    List<HistoryMsg> selectByFromAndTo(HistoryMessageModel model);

    List<HistoryMsg> selectByOwnerJid(String ownerJid);

}