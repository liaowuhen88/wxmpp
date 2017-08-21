package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.request.AppKeyVisitorLoginBean;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.AppCustomer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.AppKeyService;
import com.baodanyun.websocket.service.CustomerDispatcherTactics;
import com.baodanyun.websocket.util.PropertiesUtil;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/6/30.
 */
@Service
public class AppKeyServiceImpl implements AppKeyService {

    private static Map<String, Visitor> visitors = new ConcurrentHashMap<>();
    Map<String, String> map = PropertiesUtil.get(this.getClass().getClassLoader(), "config.properties");
    @Autowired
    private CustomerDispatcherTactics customerDispatcherTactics;

    /**
     * 如果校验出错，抛异常
     *
     * @param re
     * @return
     */
    @Override
    public AppCustomer getCustomerByAppKey(AppKeyVisitorLoginBean re, String url) throws BusinessException {

        AbstractUser customer = customerDispatcherTactics.getCustomer(re.getId());

        AppCustomer au = new AppCustomer();
        au.setId(customer.getId());
        au.setLoginUsername(customer.getLoginUsername());
        au.setUserName(customer.getUserName());
        au.setLoginTime(System.currentTimeMillis());

        au.setIcon("http://www.gx8899.com/uploads/allimg/2016060716/1-160226194S9.png");
        au.setSocketUrl(url + "/sockjs/newVisitor");
        au.setOssUrl(map.get("oss.upload"));
        au.setToken(UUID.randomUUID().toString());

        return au;
    }

    @Override
    public Visitor getVisitor(AppKeyVisitorLoginBean re, String token) throws BusinessException {
        if (StringUtils.isEmpty(re.getId())) {
            throw new BusinessException("id is null");
        }

        String id = re.getId();
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException("用户id不能为空");
        } else {
            id = id.toLowerCase();
        }
        Visitor au = new Visitor();
        au.setId(XMPPUtil.nameToJid(id));
        au.setLoginUsername(id);
        au.setLoginTime(System.currentTimeMillis());
        au.setOpenId(re.getId());
        au.setUserName(re.getNickname());
        au.setIcon(re.getAvatar());
        au.setNickName(re.getNickname());
        au.setPassWord("00818863ff056f1d66c8427836f94a87");

        visitors.put(token, au);
        return au;
    }

    @Override
    public Visitor getVisitorByToken(String token) throws BusinessException {
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException("token is null");
        }
        return visitors.get(token);
    }


}
