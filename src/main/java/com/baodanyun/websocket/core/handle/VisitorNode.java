/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.conf.CustomerConf;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Customer;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.HistoryUserServer;
import com.baodanyun.websocket.service.VcardService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.service.XmppUserOnlineServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Queue;

*/
/**
 * Created by yutao on 2016/9/7.
 * 如果访客节点上线，客服节点会收到订阅消息
 *//*

@Service
@Scope("prototype")
public class VisitorNode extends Node {
    protected XmppUserOnlineServer xmppUserOnlineServer = SpringContextUtil.getBean("xmppUserOnlineServer", XmppUserOnlineServer.class);

    public HistoryUserServer historyUserServer = SpringContextUtil.getBean("historyUserServer", HistoryUserServer.class);

    protected VcardService vcardService = SpringContextUtil.getBean("vcardService", VcardService.class);


    //当前节点是否有等待
    protected NodeWaitManage nwg = new NodeWaitManage();

    //访客回调事件
    protected VisitorEventHandelWebSocket visitorEvent = null;

    private CustomerNode bindCustomerNode = null;

    public VisitorNode(Visitor visitor) {
        super(visitor);
        this.visitorEvent = new VisitorEventHandelWebSocket();
    }

    //下线后 会调用客服的卸载节点方法
    @Override
    protected void offlineCallback() throws InterruptedException {
        nwg.endRecord();
        if (bindCustomerNode.uninstallVisitorNode(this)) {
            NodeManager.getVisitorNodeMap().remove(this.getId());
        }
    }

    public boolean xmppLogin() throws IOException, XMPPException, SmackException {
        return xmppServer.login(this.getBindUser(), XmppServer.VISITOR);
    }

    public boolean customerIsOnline() throws BusinessException {
        Visitor visitor = (Visitor) this.getBindUser();
        CustomerNode customerNode = NodeManager.getCustomerNodeMap().get(visitor.getCustomerJid());
        boolean customerIsOnline;
        if (null != customerNode && customerNode.isOnline()) {
            logger.info("客服id[" + visitor.getCustomerJid() + "] pc 端online");
            customerIsOnline = true;
        } else {
            try {
                customerIsOnline = xmppUserOnlineServer.isOnlineByJid(visitor.getCustomerJid());
            } catch (IOException e) {
                throw new BusinessException("xmpp login erroe");
            }
        }
        return customerIsOnline;
    }

    public boolean login() throws SmackException, XMPPException, IOException, BusinessException {
        Visitor visitor = (Visitor) this.getBindUser();
        boolean customerIsOnline = customerIsOnline();
        boolean isLoginDone;
        if (customerIsOnline) {
            isLoginDone = xmppLogin();
            if (isLoginDone) {
                vcardService.InitVisitor(visitor);
                NodeManager.getVisitorNodeMap().put(this.getBindUser().getId(), this);

                xmppServer.roster(visitor.getId(), visitor.getCustomerJid());
            } else {
                throw new BusinessException("登录失败");
            }
        } else {
            throw new BusinessException("customer is not online");
        }

        return isLoginDone;
    }


    //加入队列时的处理回调
    @Override
    protected boolean joinQueue() throws InterruptedException {
        //如果是刷新 则直接 发送登录成功给访客
        //如果访客在排队 刷新时候 会忽略这条消息
        synchronized (bindCustomerNode.getLock()) {
            if (bindCustomerNode.getOnlineQueue().contains(this)) {
                bindCustomerNode.customerEvent.visitorAtOnlineQueueSuccess(bindCustomerNode, this);
                visitorEvent.visitorAtOnlineQueueSuccess(bindCustomerNode, this);
                return true;
            } else if (bindCustomerNode.getWaitQueue().contains(this)) {
                bindCustomerNode.customerEvent.visitorAtWaitQueueSuccess(bindCustomerNode, this);
                visitorEvent.visitorAtWaitQueueSuccess(bindCustomerNode, this);
                return true;
            } else if (bindCustomerNode.getBackUpQueue().contains(this)) {
                bindCustomerNode.customerEvent.visitorAtBackQueueSuccess(bindCustomerNode, this);
                visitorEvent.visitorAtBackUpQueueSuccess(bindCustomerNode, this);
                return true;
            } else {
                return join();
            }
        }
    }

    private boolean join() throws InterruptedException {
        boolean isJoinSuccess = false;
        //获取客服的配置
        CustomerConf customerConf = (CustomerConf) bindCustomerNode.conf;
        AbstractUser customer = bindCustomerNode.getBindUser();
        //这里并不包含backup队列 因为backup队列是被手动调整的
        if (bindCustomerNode.getOnlineQueue().size() < customerConf.getOnlineQueueDefaultSize()) {
            //线上队列有位置 则直接进入到线上队列
            if (bindCustomerNode.mount2Online(this.getBindUser())) {
                //如果上线 重置等待对象
                logger.info( "保存到缓存--->"+historyUserServer.add(customer.getId(),this.getBindUser()));

                nwg.endRecord();
                //通知自己和客服
                bindCustomerNode.customerEvent.visitorAtOnlineQueueSuccess(bindCustomerNode, this);
                visitorEvent.visitorAtOnlineQueueSuccess(bindCustomerNode, this);
                isJoinSuccess = true;
            } else {
                //对象排队线上队列失败
                bindCustomerNode.customerEvent.visitorAtOnlineQueueFail(bindCustomerNode, this);
                visitorEvent.visitorAtOnlineQueueFail(bindCustomerNode, this);
            }
        } else {
            int index = bindCustomerNode.mount2Wait(this.getBindUser());
            historyUserServer.delete(customer.getId(),this.getBindUser());
            if (index != 0) {
                //开始记录等待状态
                nwg.beginRecordWaitTime(index);
                //通知自己和客服
                bindCustomerNode.customerEvent.visitorAtWaitQueueSuccess(bindCustomerNode, this);
                visitorEvent.visitorAtWaitQueueSuccess(bindCustomerNode, this);
                isJoinSuccess = true;
            } else {
                //对象排队等待队列失败
                bindCustomerNode.customerEvent.visitorAtWaitQueueFail(bindCustomerNode, this);
                visitorEvent.visitorAtWaitQueueFail(bindCustomerNode, this);
            }
        }
        return isJoinSuccess;
    }


    public void onlineInit(Visitor visitor) throws BusinessException {

        CustomerNode customerNode = NodeManager.getCustomerNodeMap().get(visitor.getCustomerJid());
        this.getDestSet().add(visitor.getCustomerJid());


        //判断是否是PC还是app登录
        //如果是pc登录
        if (null != customerNode && customerNode.isOnline()) {
            this.setBindCustomerNode(customerNode);
        } else {
            //非pc登录   需判断是否是登录状态
            CustomerNode cn = NodeManager.getCustomerNodeMap().get(visitor.getCustomerJid());
            if (cn == null) {
                Customer customer = new Customer();
                customer.setId(visitor.getCustomerJid());
                cn = new CustomerNode(customer);
                cn.offline();
                NodeManager.getCustomerNodeMap().put(visitor.getCustomerJid(), cn);
            }
            this.setBindCustomerNode(cn);
        }
    }

    //更新当前节点到线上节点
    //可以把等待节点移动到Backup队列
    public boolean updateNodeToBackUp() {

        try {
            synchronized (bindCustomerNode.getLock()) {
                //获取自己的客服
                CustomerNode myCustomerNode = this.bindCustomerNode;
                //在队列中卸载当前对象
                myCustomerNode.uninstallVisitorNode(this);
                //获取备份队列
                Queue<AbstractUser> backUpQueue = myCustomerNode.getBackUpQueue();
                //通知客服和访客
                if (backUpQueue.add(this.getBindUser())) {
                    myCustomerNode.customerEvent.visitorAtBackQueueSuccess(bindCustomerNode, this);
                    visitorEvent.visitorAtBackUpQueueSuccess(bindCustomerNode, this);
                    return true;
                } else {
                    myCustomerNode.customerEvent.visitorAtBackQueueFail(bindCustomerNode, this);
                    visitorEvent.visitorAtBackUpQueueFail(bindCustomerNode, this);
                }
            }
        } catch (Exception e) {
            logger.info(e);
        }

        return false;
    }

    */
/**
     * 当前节点的等待时长管理
     *//*

    class NodeWaitManage {

        private Boolean isWait = false;
        private Integer waitIndex = 0;

        public NodeWaitManage() {
        }

        public void beginRecordWaitTime(Integer waitIndex) {
            //开始记录等待时间
            this.isWait = true;
            this.waitIndex = waitIndex;
        }

        public void endRecord() {
            isWait = false;
            waitIndex = 0;
        }

        public Boolean getIsWait() {
            return isWait;
        }

        public void setIsWait(Boolean isWait) {
            this.isWait = isWait;
        }

        public Integer getWaitIndex() {
            return waitIndex;
        }

        public void setWaitIndex(Integer waitIndex) {
            this.waitIndex = waitIndex;
        }
    }

    public NodeWaitManage getNwg() {
        return nwg;
    }

    public CustomerNode getBindCustomerNode() {
        return bindCustomerNode;
    }

    public void setBindCustomerNode(CustomerNode bindCustomerNode) {
        this.bindCustomerNode = bindCustomerNode;
    }
}
*/
