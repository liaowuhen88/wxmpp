package com.baodanyun.websocket.node;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;
import com.baodanyun.websocket.enums.MsgStatus;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;

/**
 * Created by think on 2017-06-17.
 */
public class ChatNodeAdaptation {
    private ChatNode chatNode;

    public ChatNodeAdaptation( ChatNode chatNode){
        this.chatNode = chatNode;
    }

    public boolean synchronizationMsg(String id,Msg msg){
        return chatNode.synchronizationMsg(id,msg);

    }

    public AbstractUser getAbstractUser(){
        return chatNode.getAbstractUser();
    }

    public void sendMessageTOXmpp(Message xmppMsg) throws SmackException.NotConnectedException {
         chatNode.sendMessageTOXmpp(xmppMsg);

    }

    public boolean messageCallBack(AbstractUser abstractUser, MsgStatus msgStatus) throws SmackException.NotConnectedException, InterruptedException {
        return chatNode.messageCallBack(abstractUser, msgStatus);
    }

    /**
     * 获取realTo 地址
     * @return
     * @throws SmackException.NotConnectedException
     */
    public String getRealTo() {
        if(chatNode instanceof VisitorChatNode){
           return  ((VisitorChatNode) chatNode).getCurrentChatNode().getAbstractUser().getId();
        }
        return null;
    }


    /**
     * 获取realTo 地址
     *
     * @return
     * @throws SmackException.NotConnectedException
     */
    public AbstractUser getCustomer() {
        if (chatNode instanceof VisitorChatNode) {
            return ((VisitorChatNode) chatNode).getCurrentChatNode().getAbstractUser();
        }
        return  null;
    }
}
