package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.AppKeyService;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/30.
 */
@Service
public class AppKeyServiceImpl implements AppKeyService {

    /**
     * 如果校验出错，抛异常
     *
     * @param appkey
     * @return
     */
    @Override
    public AbstractUser getByAppKey(String appkey) throws BusinessException {

        if (StringUtils.isEmpty(appkey)) {
            throw new BusinessException("appKey is null");
        }
        AbstractUser au = new AbstractUser();
        au.setId(XMPPUtil.nameToJid(appkey));
        au.setLoginUsername(appkey);
        au.setOpenId(appkey);
        au.setPassWord("00818863ff056f1d66c8427836f94a87");
        au.setUserName("客服");
        au.setIcon("http://www.gx8899.com/uploads/allimg/2016060716/1-160226194S9.png");
        return au;
    }
}
