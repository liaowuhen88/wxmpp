package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.request.AppKeyVisitorLoginBean;
import com.baodanyun.websocket.bean.user.AppCustomer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.AppKeyService;
import com.baodanyun.websocket.util.PropertiesUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by liaowuhen on 2017/6/30.
 */
@Service
public class AppKeyServiceImpl implements AppKeyService {
    Map<String, String> map = PropertiesUtil.get(this.getClass().getClassLoader(), "config.properties");

    /**
     * 如果校验出错，抛异常
     *
     * @param appkey
     * @return
     */
    @Override
    public AppCustomer getCustomerByAppKey(String appkey, String url) throws BusinessException {

        if (StringUtils.isEmpty(appkey)) {
            throw new BusinessException("appKey is null");
        }
        AppCustomer au = new AppCustomer();
        au.setId(XMPPUtil.nameToJid(appkey));
        au.setLoginUsername(appkey);
        au.setOpenId(appkey);
        au.setUserName("客服");
        au.setIcon("http://www.gx8899.com/uploads/allimg/2016060716/1-160226194S9.png");
        au.setCustomerIsOnline(true);
        au.setSocketUrl(url + "/sockjs/newVisitor");
        au.setOssUrl(map.get("oss.upload"));


        return au;
    }

    @Override
    public Visitor getVisitor(AppKeyVisitorLoginBean re) throws BusinessException {
        if (StringUtils.isEmpty(re.getId())) {
            throw new BusinessException("id is null");
        }
        Visitor au = new Visitor();
        au.setId(XMPPUtil.nameToJid(re.getId()));
        au.setLoginUsername(re.getId());
        au.setOpenId(re.getId());
        au.setUserName("客服");
        au.setIcon(re.getAvatar());
        au.setNickName(re.getNickname());
        au.setPassWord("00818863ff056f1d66c8427836f94a87");
        return au;
    }
}
