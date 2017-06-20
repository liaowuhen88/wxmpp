package com.baodanyun.websocket.controller;

import com.baodanyun.websocket.bean.NodeStatues;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by yutao on 2016/10/4.
 * 用于监控  ws Session 以及xmpp的状态信息
 */
@RestController
public class MonitorApi extends BaseController {
    protected static Logger logger = LoggerFactory.getLogger(MonitorApi.class);

    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private XmppServer xmppServer;
    @Autowired
    private UserServer userServer;

    @Autowired
    private UserCacheServer userCacheServer;

    @Autowired
    private CustomerDispatcherService customerDispatcherService;

    public List<NodeStatues> getNodeStatues(String id, Map<String, ? extends AbstractUser> map) throws BusinessException {

        if (!StringUtils.isEmpty(id)) {
            List<NodeStatues> li = new ArrayList<>();
            if (null != map.get(id)) {
                li.add(getNodeStatuesFromNode(map.get(id)));
                return li;
            } else {
                throw new RuntimeException("id:" + id + "------Visitor node is null");
            }
        } else {
            List<NodeStatues> li = new ArrayList<>();
            Collection<? extends AbstractUser> co = map.values();

            if (!CollectionUtils.isEmpty(co)) {

                Iterator<? extends AbstractUser> it = co.iterator();
                while (it.hasNext()) {
                    AbstractUser node = it.next();
                    NodeStatues ns = getNodeStatuesFromNode(node);
                    li.add(ns);
                }

                return li;
            } else {
                throw new BusinessException("map is null");
            }
        }
    }


    public NodeStatues getNodeStatuesFromNode(AbstractUser user) {
        NodeStatues ns = new NodeStatues();

       /* ns.setId(user.getId());
        if (xmppServer.isConnected(user.getId())) {
                ns.setXmppIsOnline(true);
            if (xmppServer.isAuthenticated(user.getId())) {
                    ns.setXmppIsAuthenticated(true);
                }
            }

            if (user instanceof Visitor) {
                AbstractUser customer;
                try {
                    customer = userCacheServer.getCustomerByVisitorOpenId(user.getOpenId());
                    String to = customer.getId();
                    ns.setTo(to);
                } catch (BusinessException e) {
                    logger.error("error", e);
                }

            } else if (user instanceof Customer) {
                Set<AbstractUser> onlineQueue =  userCacheServer.get(CommonConfig.USER_ONLINE,user.getId());

                if (onlineQueue.size() > 0) {
                    Iterator<AbstractUser> it = onlineQueue.iterator();
                    while (it.hasNext()) {
                        AbstractUser vn = it.next();
                        ns.getOnlineQueue().add(vn.getId());
                    }
                }
            }

        if (webSocketService.hasH5Connected(user.getId())) {
            ns.setWsIsOnline(true);
        }*/


        return ns;

    }
}
