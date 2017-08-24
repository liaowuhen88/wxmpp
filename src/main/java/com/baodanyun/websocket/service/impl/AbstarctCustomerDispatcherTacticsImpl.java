package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CustomerDispatcherTactics;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/6/29.
 */

public abstract class AbstarctCustomerDispatcherTacticsImpl implements CustomerDispatcherTactics {
    private static final Logger logger = LoggerFactory.getLogger(AbstarctCustomerDispatcherTacticsImpl.class);
    // 当前接入用户客服   key 为ID
    private static final Map<String, AbstractUser> acceptCustomers = new ConcurrentHashMap();
    // 当前不接入用户客服    key 为ID
    private static final Map<String, AbstractUser> refuseCustomers = new ConcurrentHashMap<>();
    private static Integer count = 0;
    @Autowired
    private XmppServer xmppServer;

    /**
     * 返回值可能为空
     * <p/>
     * 根据Jid获取当前接入用户客服
     *
     * @param jid
     * @return
     * @throws BusinessException
     */
    @Override
    public AbstractUser getCustomerAcceptByJidOnline(String jid) throws BusinessException {
        if (!StringUtils.isEmpty(jid) && xmppServer.isAuthenticated(jid)) {
            AbstractUser customer = acceptCustomers.get(jid);
            if (null != customer) {
                return customer;
            }
        } else {
            logger.info(" jid:{}  customer is not online", jid);
        }
        return null;
    }

    @Override
    public AbstractUser getCustomerRefusedJidOnline(String jid) throws BusinessException {
        if (!StringUtils.isEmpty(jid) && xmppServer.isAuthenticated(jid)) {
            AbstractUser customer = refuseCustomers.get(jid);
            if (null != customer) {
                return customer;
            }
        } else {
            logger.info(" jid:{}  customer is not online", jid);
        }
        return null;
    }


    @Override
    public synchronized AbstractUser saveCustomer(AbstractUser customer) {
        // 接入用户
        if (!StringUtils.isEmpty(((Customer) customer).getAccessType()) && ((Customer) customer).getAccessType().equals("2")) {
            synchronized (acceptCustomers) {
                acceptCustomers.put(customer.getId(), customer);
                return customer;
            }
        } else {
            logger.info("不接入用户");
            synchronized (refuseCustomers) {
                refuseCustomers.put(customer.getId(), customer);
                return customer;
            }
        }


    }

    /**
     * 返回值可能为空
     *
     * @param cJid
     * @return
     */
    @Override
    public synchronized AbstractUser deleteCustomer(String cJid) {
        AbstractUser customers = acceptCustomers.remove(cJid);
        if (null != customers) {
            logger.info("deleteCustomer from acceptCustomers [" + cJid + "]");
            return customers;
        }

        customers = refuseCustomers.remove(cJid);
        if (null != customers) {
            logger.info("deleteCustomer from refuseCustomers [" + cJid + "]");
            return customers;
        }

        return null;
    }

    @Override
    public Collection getCustomerAccept() {
        return acceptCustomers.values();
    }


    /**
     * 随机获取在线客服
     *
     * @return
     * @throws BusinessException
     */
    public AbstractUser getAcceptDispatcher() throws BusinessException {
        synchronized (acceptCustomers) {
            int size = acceptCustomers.size();
            Collection<AbstractUser> values = acceptCustomers.values();
            List<AbstractUser> list = new ArrayList(values);
            if (size > 0) {
                count++;
                while (count >= size) {
                    count = count - size;
                }
                AbstractUser customer = list.get(count);
                if (xmppServer.isAuthenticated(customer.getId())) {
                    return customer;
                } else {
                    logger.info("{} 已经离线", customer.getId());
                    deleteCustomer(customer.getId());
                    return getAcceptDispatcher();
                }

            }
        }

        return null;
    }

    @Override
    public AbstractUser getDefaultCustomer() {
        Customer cu = new Customer();
        cu.setLoginUsername(Config.controlId);
        cu.setOpenId(Config.controlId);
        cu.setId(XMPPUtil.nameToJid(Config.controlId));
        return cu;
    }
}
