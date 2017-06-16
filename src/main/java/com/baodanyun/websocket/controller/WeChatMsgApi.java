package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.bean.response.WeChatMsgStatisticsAdapter;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.WechatMsgService;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
public class WeChatMsgApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(WeChatMsgApi.class);

    @Autowired
    private WechatMsgService wechatMsgService;

    @RequestMapping(value = "weChatMsg/statistics")
    public void backupQueue(String jid, String startDate, String endDate, HttpServletRequest request, HttpServletResponse response) {
        Response msgResponse = new Response();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date daStart = formatter.parse(startDate);
            Date daEnd = formatter.parse(endDate);

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(daStart);
            calendarStart.add(Calendar.DATE, 30);

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(daEnd);

            Collection<WeChatMsgStatisticsAdapter> was = new ArrayList<>();
            if (calendarStart.after(calendarEnd)) {
                calendarStart.add(Calendar.DATE, -30);
                calendarEnd.add(Calendar.DATE, 1);
                while (calendarEnd.after(calendarStart)) {
                    List<WeChatMsgStatistics> li = wechatMsgService.statistics(jid, calendarStart.getTime());
                    Collection<WeChatMsgStatisticsAdapter> list = wechatMsgService.statisticsAdapter(li);
                    was.addAll(list);

                    calendarStart.add(Calendar.DATE, 1);
                }
            } else {
                throw new BusinessException("最多只能查询一个月");
            }


            msgResponse.setData(was);
            msgResponse.setSuccess(true);

        } catch (Exception e) {
            msgResponse.setSuccess(false);
            msgResponse.setMsg(e.getMessage());
            logger.error("error", e);

        }

        Render.r(response, XMPPUtil.buildJson(msgResponse));
    }

}
