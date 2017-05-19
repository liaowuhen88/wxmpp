package com.baodanyun.websocket.bean.msg.active;

/**
 * Created by yutao on 2016/11/7.
 * 把一个访客 从一个客服移动到另一客服中
 */
public class MoveAction extends AbstractAction {
    public MoveAction(String fromJid, String toJid) {
        setType(Type.active.toString());
        setActionType(ActionType.move);
        this.setFrom(fromJid);
        this.setTo(toJid);
    }
}
