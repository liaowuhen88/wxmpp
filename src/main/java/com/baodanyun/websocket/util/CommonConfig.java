package com.baodanyun.websocket.util;

/**
 * Created by liaowuhen on 2017/2/16.
 */
public class CommonConfig {

    //业务线定义 otype
    public static final int Event_OType_OTHER = 0; //其他业务线
    public static final int Event_OType_H5_KF = 1; //h5客服业务线
    public static final int Event_OType_WX_KF = 2; //微信客服业务线

    //用户交互事件的事件类型   event
    public static final String MSG_BIZ_KF_ENTER = "0101"; //进入客服
    public static final String MSG_BIZ_KF_CHAT = "010101"; //与客服聊天
    public static final String MSG_BIZ_KF_WX_CHAT = "010103"; //与客服聊天

    public static final String MSG_BIZ_KF_QUIT = "0102"; //退出客服
    public static final String MSG_BIZ_KF_LEAVE_MESSAGE = "0103"; //用户留言

    public static final String LOGIN_FROM_H5 = "0109"; //用户从H5进入
    public static final String LOGIN__FROM_WE_CHAT_ACTIVE = "0108"; //用户主动从微信进入
    public static final String LOGIN__FROM_WE_CHAT_PASSIVE = "0106"; //用户被动从微信进入

    public static final String MSG_SOURCE_H5 = "010901"; //消息来源于H5
    public static final String MSG_SOURCE_WE_CHAT_ACTIVE = "010801"; //用户消息主动从来源于微信
    public static final String MSG_SOURCE_WE_CHAT_PASSIVE = "010601"; //用户消息被动从来源于H5


    // 缓存常量

    public static final String USER_INFO_KEY = "KF_USER_INFO_KY_";//用户信息
    public static final String USER_ACCOUNT_KEY = "KF_USER_ACCOUNT_KY_";//UserAccount
    public static final String USER_VISITOR= "USER_VISITOR";//客服
    public static final String USER_CUSTOMER = "USER_CUSTOMER";//访客
    //public static final String USER_HISTORY = "USER_HISTORY";//历史聊天记录人
    public static final String USER_ONLINE = "USER_ONLINE";//在线
    public static final String USER_WAIT = "USER_WAIT";//等待队列
    public static final String USER_BACKUP= "USER_BACKUP";//备份

    public static final String USER_COMPANY_KEY = "USER_COMPANY_KEY";//订单信息
    public static final String USER_ORDER_KEY = "USER_ORDER_KEY_";//订单信息
    public static final String USER_CLAIMS_KEY = "USER_CLAIMS_KEY_";//理赔信息
    public static final String USER_CONTRACT_KEY = "USER_CONTRACT_KEY_";//合同信息
    public static final String USER_OPENID_KEY = "USER_OPENID_KEY";//openId

}
