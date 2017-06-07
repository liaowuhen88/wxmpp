package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.WechatMsg;

public interface WechatMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatMsg record);

    int insertSelective(WechatMsg record);

    WechatMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatMsg record);

    int updateByPrimaryKeyWithBLOBs(WechatMsg record);

    int updateByPrimaryKey(WechatMsg record);
}