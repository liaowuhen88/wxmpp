package com.baodanyun.websocket.service.dubbo.impl;

import com.baodanyun.websocket.bean.dubbo.QRCodeContent;
import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;
import com.baodanyun.websocket.service.dubbo.WxQRCodeService;
import com.baodanyun.websocket.util.DubboPostSendMsgUtils;
import com.baodanyun.websocket.util.DubboServiceConfig;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.DefaultResponse;
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
        String content = JSONUtil.toJson(qc);
        try {
            DefaultResponse response = DubboPostSendMsgUtils.send(DubboServiceConfig.SM_GetQRCodeTempUrl, content);
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
