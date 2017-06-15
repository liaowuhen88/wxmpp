package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CustomerDispatcherService;
import com.baodanyun.websocket.service.UserCacheServer;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.Config;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liaowuhen on 2017/4/25.
 */

@Service
public class CustomerDispatcherServiceImpl implements CustomerDispatcherService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDispatcherServiceImpl.class);
    private static final Map<String, AbstractUser> customers = new ConcurrentHashMap();

    private static final Vector<String> cids = new Vector<>();
    private static AtomicInteger count = new AtomicInteger();

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private XmppServer xmppServer;

    @Override
    public AbstractUser getCustomer(String openId) throws BusinessException {
        String cid = userCacheServer.getCustomerIdByVisitorOpenId(openId);

        logger.info("openid:[" + openId + "]--------cid[" + cid + "]");
        if (!StringUtils.isEmpty(cid) && xmppServer.isAuthenticated(cid)) {
            AbstractUser customer = userCacheServer.getUserCustomer(cid);

            if (null != customer) {
                return customer;
            }
        }

        return getDispatcher(openId);
    }

    @Override
    public AbstractUser getCustomerByJid(String jid) throws BusinessException {
        if (!StringUtils.isEmpty(jid) && xmppServer.isAuthenticated(jid)) {
            AbstractUser customer = customers.get(jid);

            if (null != customer) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public AbstractUser saveCustomer(AbstractUser customer) {
        synchronized (cids) {
            cids.add(customer.getId());
            customers.put(customer.getId(), customer);
            return customer;
        }
    }

    @Override
    public AbstractUser deleteCustomer(String cJid) {
        logger.info("deleteCustomer[" + cJid + "]");
        synchronized (cids) {
            cids.remove(cJid);
            return customers.remove(cJid);
        }
    }

    @Override
    public AbstractUser getDispatcher(String openId) throws BusinessException {
        synchronized (cids) {
            if (cids.size() > 0) {
                count.getAndIncrement();
                while (count.get() >= cids.size()) {
                    count.getAndAdd(-cids.size());
                }

                String cid = cids.get(count.get());

                if (xmppServer.isAuthenticated(cid)) {
                    logger.info(cid);

                    userCacheServer.addVisitorCustomerOpenId(openId, cid);

                    return customers.get(cid);
                } else {
                    deleteCustomer(cid);
                    return getDispatcher(openId);
                }

            } else {
                Customer cu = new Customer();
                cu.setLoginUsername(Config.controlId);
                cu.setId(XMPPUtil.nameToJid(Config.controlId));

                userCacheServer.addVisitorCustomerOpenId(openId, cu.getId());

                return cu;
            }
        }

    }

    @Override
    public Collection getCustomerAccept() {
        return customers.values();
    }

}
