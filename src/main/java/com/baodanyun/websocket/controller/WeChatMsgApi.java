package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.Response;
import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.service.WechatMsgService;
import com.baodanyun.websocket.util.Render;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by yutao on 2016/10/4.
 */
@RestController
public class WeChatMsgApi extends BaseController {
    protected static Logger logger = Logger.getLogger(WeChatMsgApi.class);

    @Autowired
    private WechatMsgService wechatMsgService;

    @RequestMapping(value = "weChatMsg/statistics")
    public void backupQueue(String jid, String date, HttpServletRequest request, HttpServletResponse response) {
        Response msgResponse = new Response();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date da = formatter.parse(date);

            List<WeChatMsgStatistics> li = wechatMsgService.statistics(jid, da);
            msgResponse.setData(li);
            msgResponse.setSuccess(true);

        } catch (Exception e) {
            msgResponse.setSuccess(false);
            msgResponse.setMsg(e.getMessage());
            logger.error(e);

        }

        Render.r(response, XMPPUtil.buildJson(msgResponse));
    }

}
