package com.baodanyun.websocket.bean.msg.msg;

import com.baodanyun.websocket.bean.msg.Msg;

/**
 * Created by yutao on 2016/7/12.
 * visitor回执消息 确认visitor已经收到消息
 */
public class ReceiptMsg extends Msg {

    public ReceiptMsg(String content){
        setContentType(MsgContentType.receiptMsg.toString());
        setContent(content);
    }


}
