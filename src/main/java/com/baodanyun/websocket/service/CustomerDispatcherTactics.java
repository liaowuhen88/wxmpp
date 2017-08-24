package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;

import java.util.Collection;

/**
 * Created by liaowuhen on 2017/6/29.
 */
public interface CustomerDispatcherTactics {

    /**
     * 获取用户策略   必须保证customer不能为空
     * @param openId
     * @return
     * @throws BusinessException
     */
    AbstractUser getCustomer(String openId) throws BusinessException;

    AbstractUser getCustomerAcceptByJidOnline(String jid) throws BusinessException;

    AbstractUser getCustomerRefusedJidOnline(String jid) throws BusinessException;

    AbstractUser saveCustomer(AbstractUser customer);

    AbstractUser deleteCustomer(String cJid);

    Collection getCustomerAccept();

    AbstractUser getDefaultCustomer();

}
