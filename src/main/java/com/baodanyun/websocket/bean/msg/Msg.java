package com.baodanyun.websocket.bean.msg;

import com.baodanyun.websocket.bean.msg.msg.ImgMsg;
import com.baodanyun.websocket.bean.msg.msg.ReceiptMsg;
import com.baodanyun.websocket.bean.msg.msg.TextMsg;
import com.baodanyun.websocket.bean.msg.status.StatusMsg;
import com.baodanyun.websocket.core.common.Common;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by yutao on 2016/7/20.
 * 顶层消息 描述了当前消息是动作 还是消息
 */
public class Msg implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Msg.class);
    // 表示用户从那个入口接入
    //  0 默认h5客服端   1 微信直接聊天入口
    private Integer toType;
    private String token;
    private String openId;
    private String fromName;//from的昵称
    private String icon;  //头像
    private String from;//jid
    private String to;//jid
    private String toName;
    private String content;
    private String contentType;
    private String messageId;
    private String id;
    private Long ct;
    private String type;
    private String msgType;   //synchronize
    private boolean isRead;//TODO 是否已经被回执
    private Long readCt;//阅读时间

    private Integer source; //终端来源标识
    private String sourceDesc; //端来源描述
    private String serialNumber; //批次

    public Msg(String content) {
        this.content = content;
    }

    public Msg() {
    }

    public static Msg handelMsg(String bodyMsg) {
        Msg msg = null;
        try {
            if (null != bodyMsg) {
                Gson gson = new Gson();
                Msg topMsg = gson.fromJson(bodyMsg, Msg.class);
                if (Msg.Type.msg.toString().equals(topMsg.getType())) {
                    Msg abstractMsg = gson.fromJson(bodyMsg, Msg.class);
                    if (Msg.MsgContentType.text.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, TextMsg.class);
                    } else if (Msg.MsgContentType.audio.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.file.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.video.toString().equals(abstractMsg.getContentType())) {

                    } else if (Msg.MsgContentType.image.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, ImgMsg.class);
                    } else if (Msg.MsgContentType.receiptMsg.toString().equals(abstractMsg.getContentType())) {
                        return gson.fromJson(bodyMsg, ReceiptMsg.class);
                    } else {
                        return null;
                    }
                } else if (Msg.Type.active.toString().equals(topMsg.getType())) {
                } else if (Msg.Type.status.toString().equals(topMsg.getType())) {
                    return gson.fromJson(bodyMsg, StatusMsg.class);
                } else {
                }
            }
        } catch (Exception e) {
            logger.error("error", e);
        }
        return msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getCt() {
        return ct;
    }

    public void setCt(Long ct) {
        this.ct = ct;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = String.format(Common.MSG_ID_TPL, id, type);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.id = String.format(Common.MSG_ID_TPL, System.currentTimeMillis(), type);
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Long getReadCt() {
        return readCt;
    }

    public void setReadCt(Long readCt) {
        this.readCt = readCt;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getToType() {
        return toType;
    }

    public void setToType(Integer toType) {
        this.toType = toType;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    //receiptMsg 回执消息
    public enum MsgContentType {
        text, video, file, audio, image, receiptMsg
    }

    //消息种类
    public enum Type {
        active, msg, status
    }

}
