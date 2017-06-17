package com.baodanyun.websocket.node.xmpp;

import com.baodanyun.websocket.bean.msg.Msg;
import com.baodanyun.websocket.bean.user.AbstractUser;

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

}
