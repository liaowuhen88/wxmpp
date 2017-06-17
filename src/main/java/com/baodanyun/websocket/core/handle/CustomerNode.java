/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.conf.CustomerConf;
import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.CacheService;
import com.baodanyun.websocket.service.HistoryUserServer;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;
import java.util.Set;

*/
/**
 * Created by yutao on 2016/9/7.
 * 客服节点 内部包含 两个等待队列
 * 一个是在线队列 ，一个是等待队列
 * 如果客服节点上线，访客节点会收到订阅消息
 *//*

public class CustomerTerminal extends Node {
    //每个客服 有属于自己的一个锁
    public HistoryUserServer historyUserServer = SpringContextUtil.getBean("historyUserServer", HistoryUserServer.class);

    public MsgSendService msgSendService = SpringContextUtil.getBean("msgSendService", MsgSendService.class);

    public final Object lock = new Object();

    //广播客服状态给访客
    private MulitEventSender mulitEventSender = null;
    //客服回调事件


    public CustomerTerminal(Customer customer) {
        super(customer);
        this.mulitEventSender = new MulitEventSender(this);
    }

    protected boolean joinQueue() {
        // 通知所有用户客服上线
        mulitEventSender.onlineFireToVisitor();
        return true;
    }

    */
/**
     * 卸载掉当前客服的自己访客节点
     * TODO 当前锁住这个客服 如果这里延迟 则会导致 其他访客锁住
     *
     * @param visitorNode
     * @return
     *//*


    protected boolean uninstallVisitorNode(VisitorTerminal visitorNode) throws InterruptedException {
        AbstractUser visitor = visitorNode.getBindUser();
        Set<AbstractUser> set =  historyUserServer.get(CacheService.USER_ONLINE,bindUser.getId());
        boolean uninstalled = true;
        synchronized (lock) {
            //线上队列包含访客节点
            if (set.contains(visitor)) {
                logger.debug(">>>>onlineQueue");
                if (set.remove(visitor)) {
                    logger.debug("remove done");
                    //如果线上队列移除成功，则等待队列也要相应的移除第一个放到线上队列中
                    //通知客服 移除的访客节点下线
                    getMsgSendService().sendSMMsgToVisitor(visitor,bindUser,StatusMsg.Status.offlineBackUpQueue);
                    //如果等待队列中有值，从等待队列中 poll出一个节点到线上队列
                } else {
                    uninstalled = false;
                }
            }  else {
                //既不在线上队列 也不在等待队列 这个节点是个游离状态
                //ignore
                uninstalled = false;
            }
        }
        return uninstalled;
    }

    @Override
    public boolean login() throws IOException, XMPPException, SmackException, BusinessException {

        boolean isLoginDone = xmppServer.login(this.getBindUser(), XmppServer.CUSTOMER);
        if (isLoginDone) {
            NodeManager.getCustomerNodeMap().put(this.getBindUser().getId(), this);
        }
        return isLoginDone;

    }


    protected void onlineCallback() {
        mulitEventSender.onlineFireToVisitor();
    }

    protected void offlineCallback() {
        mulitEventSender.offlineFireToVisitor();
    }

    public MulitEventSender getMulitEventSender() {
        return mulitEventSender;
    }


    public Object getLock() {
        return lock;
    }

    //存储登录属性
    protected void saveLoginProperties() {
        // customer 属性在vcar保存，
        logger.info("saveLoginProperties 忽略执行");
    }

    public MsgSendService getMsgSendService() {
        return msgSendService;
    }
}
*/
