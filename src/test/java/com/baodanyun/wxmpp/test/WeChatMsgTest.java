package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.bean.response.WeChatMsgStatistics;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.WechatMsgService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liaowuhen on 2017/6/9.
 */
public class WeChatMsgTest extends BaseTest {

    @Autowired
    private WechatMsgService wechatMsgService;

    @Test
    public void subString() throws BusinessException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse("2017-06-09");

        List<WeChatMsgStatistics> li = wechatMsgService.statistics("maqiumeng@126xmpp", date);

    }
}
