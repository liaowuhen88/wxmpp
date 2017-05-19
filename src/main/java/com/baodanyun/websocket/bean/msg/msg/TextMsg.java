package com.baodanyun.websocket.bean.msg.msg;

import com.baodanyun.websocket.bean.msg.Msg;

/**
 * Created by yutao on 2016/7/12.
 */
public class TextMsg extends Msg {

    public TextMsg(String content){
        setContentType(Msg.MsgContentType.text.toString());
        setContent(content);
    }

}
