package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.bean.response.WeChatMsgStatisticsAdapter;
import com.baodanyun.websocket.dao.WechatMsgMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;
import com.baodanyun.websocket.service.WechatMsgService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by liaowuhen on 2017/6/12.
 */

@Service
public class WechatMsgServiceImpl implements WechatMsgService {

    @Autowired
    public WechatMsgMapper wechatMsgMapper;

    @Override
    public int insert(WechatMsg record) {
        return wechatMsgMapper.insert(record);
    }

    @Override
    public List<WeChatMsgStatistics> statistics(String jid, Date date) throws BusinessException {
        if (StringUtils.isEmpty(jid)) {
            throw new BusinessException("jid can not be null");
        }
        if (null == date) {
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String startDate = DateFormatUtils.format(calendar.getTimeInMillis(), DateFormatUtils.ISO_DATE_FORMAT.getPattern());

        calendar.add(Calendar.DATE, 1);
        String endDate = DateFormatUtils.format(calendar.getTimeInMillis(), DateFormatUtils.ISO_DATE_FORMAT.getPattern());

        return statistics(jid, startDate, endDate);
    }

    @Override
    public List<WeChatMsgStatistics> statistics(String jid, String startDate, String endDate) throws BusinessException {
        return wechatMsgMapper.statistics(jid, startDate, endDate);
    }

    @Override
    public Collection<WeChatMsgStatisticsAdapter> statisticsAdapter(List<WeChatMsgStatistics> list) {
        Map<String, WeChatMsgStatisticsAdapter> map = new HashMap<>();

        if (null != list && list.size() > 0) {
            for (WeChatMsgStatistics ws : list) {
                WeChatMsgStatisticsAdapter wa = map.get(ws.getMsgTo());
                if (null == wa) {
                    wa = new WeChatMsgStatisticsAdapter();
                    wa.setMsgFrom(ws.getMsgFrom());
                    wa.setMsgTo(ws.getMsgTo());
                    wa.setSendTime(ws.getSendTime());
                    map.put(ws.getMsgTo(), wa);
                }

                if (ws.getMsgStatus() == 1) {
                    wa.setSuccessFromCount(ws.getFromCount());
                    wa.setSuccessToCount(ws.getToCount());
                } else {
                    wa.setFailFromCount(ws.getFromCount());
                    wa.setFailToCount(ws.getToCount());
                }
            }
        }
        return map.values();
    }


}
