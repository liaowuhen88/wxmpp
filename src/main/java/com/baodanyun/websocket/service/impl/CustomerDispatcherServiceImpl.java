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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2017/4/25.
 */

@Service
public class CustomerDispatcherServiceImpl implements CustomerDispatcherService {
    private static final Logger logger = Logger.getLogger(CustomerDispatcherServiceImpl.class);
    private static final Map<String, AbstractUser> customers = new ConcurrentHashMap();

    private static final Vector<String> cids = new Vector<>();
    private static Integer count = 0;

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private XmppServer xmppServer;

    @Override
    public AbstractUser getCustomer(String openId) throws BusinessException {
        String cid = userCacheServer.getCustomerIdByVisitorOpenId(openId);

        logger.info("openid:[" + openId + "]--------cid[" + cid + "]");
        if (!StringUtils.isEmpty(cid) && xmppServer.isAuthenticated(cid)) {
            return customers.get(cid);
        }

        return getDispatcher(openId);
    }

    @Override
    public AbstractUser saveCustomer(AbstractUser customer) {
        cids.add(customer.getId());
        customers.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public AbstractUser deleteCustomer(AbstractUser customer) {
        cids.remove(customer.getId());
        return customers.remove(customer.getId());
    }

    @Override
    public synchronized AbstractUser getDispatcher(String openId) {
        if (cids.size() > 0) {
            count++;
            if (count >= cids.size()) {
                count = count - cids.size();
            }

            String cid = cids.get(count);

            if (xmppServer.isAuthenticated(cid)) {
                logger.info(cid);
                return customers.get(cid);
            } else {
                deleteCustomer(customers.get(cid));
                return getDispatcher(openId);
            }

        } else {
            Customer cu = new Customer();
            cu.setLoginUsername(Config.controlId);
            cu.setId(XMPPUtil.nameToJid(Config.controlId));

            return cu;
        }
    }


}
