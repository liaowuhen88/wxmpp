/*
package com.baodanyun.websocket.core.handle;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.msg.msg.ImgMsg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.core.common.Common;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.MsgSendControl;
import com.baodanyun.websocket.service.MsgSendService;
import com.baodanyun.websocket.service.WebSocketService;
import com.baodanyun.websocket.service.XmppServer;
import com.baodanyun.websocket.util.SpringContextUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smackx.offline.OfflineMessageManager;
import org.springframework.util.CollectionUtils;

import java.util.List;

*/
/**
 * Created by liaowuhen on 2017/1/3.
 * <p/>
 * */
/**
 * 节点事件触发后 会给自己发送对应的websocket通知
 * 生命周期的webSocket通知，这里均是对自己的通知
 * init online offline 都会做出webSocket通知
 *//*


public class NodeNotifyWebSocket implements INodeEvent {
    protected static Logger logger = LoggerFactory.getLogger(LifeCycle.class);
    public WebSocketService webSocketService = SpringContextUtil.getBean("webSocketService", WebSocketService.class);
    public XmppServer xmppServer = SpringContextUtil.getBean("xmppServer", XmppServer.class);
    private MsgSendControl msgSendControl = SpringContextUtil.getBean("msgSendControl", MsgSendControl.class);
    public MsgSendService msgSendService = SpringContextUtil.getBean("visitorMsgSendServiceImpl", MsgSendService.class);

    private Node node;

    public NodeNotifyWebSocket(Node node) {
        this.node = node;
    }

    //ws初始化推送
    @Override
    public void wsInit(AbstractUser visitor,AbstractUser customer) throws InterruptedException {
        msgSendService.sendSMMsgToVisitor(visitor,customer,StatusMsg.Status.initSuccess);

        if (xmppServer.isAuthenticated(node.getBindUser().getId())) {
            try {
                xmppServer.sendPresence(node.getBindUser().getId(), Presence.Type.available);
            } catch (SmackException.NotConnectedException e) {
                logger.error("error"," xmppConnection.sendStanza(presence);", e);
            }
        }

        node.init();
    }

    public boolean sendMessageOnline(AbstractUser visitor,AbstractUser customer) {

        try {
            msgSendService.sendSMMsgToVisitor(visitor,customer,StatusMsg.Status.loginSuccess);


        } catch (InterruptedException e) {
            return false;
        }
        return true;
    }

    //只有初始化成功的节点才可以上线
    @Override
    public boolean wsOnline(AbstractUser visitor,AbstractUser customer) throws InterruptedException {
        if (sendMessageOnline(visitor,customer)) {
            //优先加入队列
            if (!node.joinQueue()) {
                return false;
            }
            //节点赋值状态
            node.run();
            //登录成功后的回调
            if(visitor instanceof Visitor){
                Visitor vi= (Visitor)visitor;
                Msg msg = msgSendControl.getMsgHello(vi);
                webSocketService.produce(msg);
            }

            return true;
        }
        return false;
    }

    //获取离线消息
    @Override
    public void pushOfflineMsg() throws BusinessException {
        //加载离线记录
        AbstractXMPPConnection xmppConnection = xmppServer.getXMPPConnectionAuthenticated(node.getBindUser().getId());
        OfflineMessageManager offlineManager = new OfflineMessageManager(xmppConnection);
        try {
            List<Message> msgList = offlineManager.getMessages();
            if (!CollectionUtils.isEmpty(msgList)) {
                for (Message message : msgList) {
                    String body = message.getBody();
                    if (StringUtils.isNotBlank(body)) {
                        Msg msgBean = XmppConnectionManager.handelMsg(body);
                        if (msgBean != null) {
                            //只接收文本,图片类型消息
                            if (msgBean instanceof TextMsg || msgBean instanceof ImgMsg) {
                                webSocketService.produce(msgBean);
                                //只有接受到消息后 才删除离线消息
                                offlineManager.deleteMessages();

                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error","offline msg error");
        }
    }

}
*/
