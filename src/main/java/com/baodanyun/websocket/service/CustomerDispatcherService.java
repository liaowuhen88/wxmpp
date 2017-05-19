package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;

/**
 * Created by liaowuhen on 2017/4/25.
 */
public interface CustomerDispatcherService {
    /**
     * 获取客服
     */

    AbstractUser getCustomer(String openId) throws BusinessException;

    AbstractUser saveCustomer(AbstractUser customer);

    AbstractUser deleteCustomer(AbstractUser customer);

    AbstractUser getDispatcher(String openId);


}
