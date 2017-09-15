package com.baodanyun.wxmpp.test;

import com.baodanyun.websocket.bean.dubbo.FollowWXResponse;
import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;
import com.baodanyun.websocket.service.dubbo.WechatService;
import com.baodanyun.websocket.service.dubbo.WxQRCodeService;
import com.baodanyun.websocket.util.JSONUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaowuhen on 2017/8/18.
 */
public class OpenCloudServiceTest extends BaseTest {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WxQRCodeService wxQRCodeService;

    @Autowired
    private WechatService wechatService;

    @Test
    public void getQrCodeTempUrl() {
        QRCodeResponse qc = wxQRCodeService.getQRCodeTempUrl("临时二维码", "", "http://m.17doubao.com/mall/index.html", (byte) 5);

        logger.info(JSONUtil.toJson(qc));
    }

    @Test
    public void getFollow() {
        String openid = "orT5yw9tG2vAjd2s7xL4UN3WehOI";
        FollowWXResponse qc = wechatService.getFollow(openid);
        logger.info(JSONUtil.toJson(qc));

    }

}
