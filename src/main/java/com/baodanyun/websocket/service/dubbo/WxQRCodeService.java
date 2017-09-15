package com.baodanyun.websocket.service.dubbo;

import com.baodanyun.websocket.bean.dubbo.QRCodeResponse;

/**
 * Created by liaowuhen on 2017/9/14.
 */
public interface WxQRCodeService {
    QRCodeResponse getQRCodeTempUrl(String name, String pic, String url, Byte type);
}
