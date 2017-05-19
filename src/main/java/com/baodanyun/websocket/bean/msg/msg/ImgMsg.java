package com.baodanyun.websocket.bean.msg.msg;

import com.baodanyun.websocket.bean.msg.Msg;

/**
 * Created by yutao on 2016/7/12.
 */
public class ImgMsg extends Msg {

    public ImgMsg(String content){
        setContentType(MsgContentType.image.toString());
        setContent(content);
    }
}
