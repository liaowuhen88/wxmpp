package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.UserCacheServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liaowuhen on 2017/6/29.
 *
 *当前客服可选择是否接入用户

 1. 用户非在线

 1.1 用户登录，首先获取绑定客服（接入此用户的客服，或者上次服务的客服）。判断是否接入用户

 1.2  如果不接入从客服列表（设置为接入用户的）中随机获取客服。

 2.用户在线

 2.1  首次接入，接入规则 首先获取绑定客服（接入此用户的客服，或者上次服务的客服）。

 2.2 如果当前绑定客服在线，则接入客服为当前绑定客服

 2.3 如果没有绑定客服，或者绑定客服下线，则从设置为接入用户的客服中随机获取客服。

 *
 */
@Service
public class CustomerDispatcherTacticsImpl extends AbstarctCustomerDispatcherTacticsImpl {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDispatcherTacticsImpl.class);

    @Autowired
    private UserCacheServer userCacheServer;

    /**
     * 获取用户策略   必须保证customer不能为空
     *
     * @param openId
     * @return
     * @throws BusinessException
     */
    @Override
    public AbstractUser getCustomer(String openId) throws BusinessException {
        String cid = userCacheServer.getCustomerIdByVisitorOpenId(openId);
        AbstractUser customer = null;
        logger.info("openid:[" + openId + "]--------cid[" + cid + "]");
        if (!StringUtils.isEmpty(cid)) {
            customer = getCustomerAcceptByJidOnline(cid);
        }

        if (null == customer) {
            customer = getAcceptDispatcher();
        }

        if (null == customer) {
            customer = getDefaultCustomer();
        }
        return customer;
    }


}
