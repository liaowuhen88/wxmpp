package com.baodanyun.websocket.service.dubbo.impl;

import com.baodanyun.websocket.bean.dubbo.QRCodeContent;
import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;
import com.baodanyun.websocket.service.dubbo.WxQRCodeService;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.Constants;
import com.doubao.open.api.DefaultRequest;
import com.doubao.open.api.DefaultResponse;
import com.doubao.open.client.DoubaoClient;
import com.doubao.open.client.env.DouBaoEnvEnum;
import com.doubao.open.client.exception.DouBaoOpenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/9/14.
 */
@Service
public class WxQRCodeServiceImpl implements WxQRCodeService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public QRCodeResponse getQRCodeTempUrl(String name, String pic, String url, Byte type) {
        QRCodeContent qc = new QRCodeContent();
        qc.setChannel("doubao");
        qc.getContent().setName(name);
        qc.getContent().setPic(pic);
        qc.getContent().setType(type);
        qc.getContent().setUrl(url);

        QRCodeResponse qr = null;
        DefaultRequest request = new DefaultRequest() {
            public String serviceName() {
                return "doubao.wechat.WxQRCodeService.getQRCodeTempUrl";
            }

            public void checkBizParams() throws DouBaoOpenException {

            }
        };
        String content = JSONUtil.toJson(qc);
        //logger.info("content {}",content);
        request.setContent(content);
        request.setSignType(Constants.MD5);
        request.setEncrypt(0);

        DouBaoEnvEnum dee;
        if (Config.bdyEnv.equals("test")) {
            DouBaoEnvEnum.TEST.setGatewayUrl("http://test2.17doubao.com/gateway.do");
            dee = DouBaoEnvEnum.TEST;
        } else {
            dee = DouBaoEnvEnum.PRD;
        }

        DoubaoClient client = new DoubaoClient(dee, Config.dubbo_appKey, Config.dubbo_appSecret, Config.dubbo_privateKey);
        try {
            DefaultResponse response = client.call(request);
            logger.info(JSONUtil.toJson(response));
            if (response != null && response.getCode() == 200) {
                qr = JSONUtil.toObject(QRCodeResponse.class, response.getContent());
            }
        } catch (DouBaoOpenException e) {
            logger.error("error", e);
        } catch (Exception e) {
            logger.error("error", e);
        }

        return qr;
    }
}
