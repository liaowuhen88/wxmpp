package com.baodanyun.websocket.bean.msg.active;

import com.baodanyun.websocket.bean.msg.Msg;

/**
 * 抽象动作
 * 当前动作包含 创建聊天窗口 销毁窗口
 */
public class AbstractAction extends Msg {

    public AbstractAction(){
        setType(Type.active.toString());
    }

    private ActionType actionType;

    public enum ActionType{
        move
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
