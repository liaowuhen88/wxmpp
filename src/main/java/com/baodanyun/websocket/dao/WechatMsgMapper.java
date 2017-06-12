package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.model.WechatMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WechatMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WechatMsg record);

    int insertSelective(WechatMsg record);

    WechatMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatMsg record);

    int updateByPrimaryKeyWithBLOBs(WechatMsg record);

    int updateByPrimaryKey(WechatMsg record);

    List<WeChatMsgStatistics> statistics(@Param("jid") String jid, @Param("startDate") String startDate, @Param("endDate") String endDate);
}