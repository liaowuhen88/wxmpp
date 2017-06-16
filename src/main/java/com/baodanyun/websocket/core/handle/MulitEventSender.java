/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.HistoryUserServer;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

*/
/**
 * 客服广播事件发送器
 * 客服上线下线 会发送给自己的访客
 *//*

public class MulitEventSender {

    protected static Logger logger = LoggerFactory.getLogger(MulitEventSender.class);
    public HistoryUserServer historyUserServer = SpringContextUtil.getBean("historyUserServer", HistoryUserServer.class);
    public MsgSendService msgSendService = SpringContextUtil.getBean("visitorMsgSendServiceImpl", MsgSendService.class);

    private ExecutorService cachedThreadPool = Executors.newFixedThreadPool(10);

    private CustomerNode customerNode;

    public MulitEventSender(CustomerNode customerNode) {
        this.customerNode = customerNode;
    }

    void doIt(final Iterator<AbstractUser> it, final boolean online) {
        cachedThreadPool.submit(new Runnable() {
            @Override
            public void run() {
                while (it.hasNext()) {
                    AbstractUser visitor = it.next();
                    if (null != visitor) {
                        VisitorNode visitorNode = NodeManager.getVisitorNodeMap().get(visitor.getId());
                        if (online) {
                            try {
                                msgSendService.sendSMMsgToVisitor(visitorNode.bindUser,customerNode.bindUser, StatusMsg.Status.customerOnline);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                msgSendService.sendSMMsgToVisitor(visitorNode.bindUser,customerNode.bindUser, StatusMsg.Status.customerOffline);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    */
/**
     * 通知所有用户客服上线
     *//*

    void onlineFireToVisitor() {
        Set<AbstractUser> set =  historyUserServer.get(CacheService.USER_ONLINE,customerNode.getBindUser().getId());

        if (null != set && set.size() > 0) {
            doIt(set.iterator(), true);
            doIt(set.iterator(), true);
            doIt(set.iterator(), true);
        } else {
            logger.info("OnlineQueue is blank");
        }

    }

    */
/**
     * 通知所有用户客服离线
     *//*

    void offlineFireToVisitor() {
        Set<AbstractUser> set =  historyUserServer.get(CacheService.USER_ONLINE,customerNode.getBindUser().getId());

        if (null != set && set.size() > 0) {
            doIt(set.iterator(), false);
            doIt(set.iterator(), false);
            doIt(set.iterator(), false);
        } else {
            logger.info("OnlineQueue is blank");
        }

    }
}*/
