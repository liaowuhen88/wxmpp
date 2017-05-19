/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.conf.Conf;
import com.baodanyun.websocket.bean.conf.CustomerConf;
import com.baodanyun.websocket.bean.conf.VisitorConf;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

*/
/**
 * Created by yutao on 2016/9/7.
 * 节点基类 抽象NODE
 * 节点生命周期为  init-->queue-->run-->offline
 *//*

public abstract class Node extends LifeCycle {

    public WebSocketService webSocketService = SpringContextUtil.getBean("webSocketService", WebSocketService.class);

    public XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);

    //节点编号
    protected String id = null;
*/
/*
    //每个节点都有基础事件通知
    protected INodeEvent eventNotify = null;*//*


    //目标
    protected Set<String> destSet = new HashSet<>();

    //当前节点待绑定的用户
    protected AbstractUser bindUser = null;



    //当前节点类型
    protected NodeType nodeType = null;

    //节点类型
    protected enum NodeType {
        CUSTOMER, VISITOR, MANAGER
    }

    //导入个人配置
    protected Conf conf;

    //处理各自的登出
    protected void nodeLogout() {
        WebSocketSession session = webSocketService.getWebSocketSession(bindUser.getId());
        //获取websession 关闭
        if (session != null) {
            HttpSession httpSession = (HttpSession) session.getHandshakeAttributes().get(Common.CUSTOMER_USER_HTTP_SESSION);
            if (httpSession != null) {
                httpSession.invalidate();
            }
        }
        //关闭ws连接
        try {
            webSocketService.closed(bindUser.getId());
        } catch (IOException e) {
            logger.info("closed session error", e);
        }
        */
/* 关闭xmpp *//*

        try {
            xmppServer.closed(bindUser.getId());
        } catch (IOException e) {
            logger.info("closed xmpp error", e);
        }
    }

    //节点加入队列时的处理方式
    protected abstract boolean joinQueue() throws InterruptedException;

    //离线后的回调
    protected abstract void offlineCallback() throws InterruptedException;


    //节点构造
    public Node(AbstractUser bindUser) {
        //节点类型赋值
        if (AbstractUser.UserType.customer == bindUser.getUserType()) {
            this.nodeType = NodeType.CUSTOMER;
            this.conf = new CustomerConf();
        } else if (AbstractUser.UserType.visitor == bindUser.getUserType()) {
            this.nodeType = NodeType.VISITOR;
            this.conf = new VisitorConf();
        }
        //绑定当前登录用户
        this.bindUser = bindUser;
        //节点id赋值
        this.id = getId();
        //构造一个系统状态消息 接受者为自己
        logger.debug("node[" + this.id + "] create done");
    }


    //统一登录地址
    //登录时 由于没有xmpp 所以 不能进行长连接提示
    //有xmpp直接返回 表示用户刷新
    public abstract boolean login() throws SmackException, XMPPException, IOException, BusinessException;


    //统一退出
    //退出有长连接 可以先做推送 后关闭
    public void logout() throws InterruptedException {
        //队列移动 关闭心跳 等
        offlineCallback();
        //session关闭
        nodeLogout();

        this.offline();
    }

    public String getId() {
        return bindUser.getId();
    }

    public void setId(String id) {
        this.id = id;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    public void setBindUser(AbstractUser bindUser) {
        this.bindUser = bindUser;
    }

    public Conf getConf() {
        return conf;
    }

    public void setConf(Conf conf) {
        this.conf = conf;
    }

    public AbstractUser getBindUser() {
        return bindUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.id == null) {
            return false;
        }

        if (!(obj instanceof Node)) {
            return false;
        }
        return this.id.equals(((Node) obj).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Set<String> getDestSet() {
        return destSet;
    }

    public void setDestSet(Set<String> destSet) {
        this.destSet = destSet;
    }

    public boolean isOnline() {

        if (xmppServer.isAuthenticated(bindUser.getId())) {
            if (webSocketService.isConnected(this.getBindUser().getId())) {
                return true;
            } else {
                logger.info("webSocketService.isConnectd() false");
                return false;
            }
        } else {
            logger.info("xmppConnection.isAuthenticated() false");
            return false;
        }

    }

    public boolean XmppIsOnline() {

        if (xmppServer.isAuthenticated(bindUser.getId())) {
            return true;
        } else {
            logger.info("xmppConnection.isAuthenticated() false");
            return false;
        }

    }

}
*/
