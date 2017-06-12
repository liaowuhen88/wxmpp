package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.dao.WechatMsgMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.model.WechatMsg;
import com.baodanyun.websocket.service.WechatMsgService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        String endDate = DateFormatUtils.format(calendar.getTimeInMillis(), DateFormatUtils.ISO_DATE_FORMAT.getPattern());

        return wechatMsgMapper.statistics(jid, startDate, endDate);
    }


}
