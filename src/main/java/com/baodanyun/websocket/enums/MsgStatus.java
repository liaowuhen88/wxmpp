package com.baodanyun.websocket.enums;

/**
 * Created by liaowuhen on 2017/4/19.
 */
public enum MsgStatus {
    //心跳状态 所有客户端 均需要满足
    heartbeat,

    //只适合客服上线，客服没有队列概念
    customerOnline,
    customerOffline,
    online,
    wait,
    backup,
    history,
    //全适用
    initSuccess,//初始化成功
    initError,//初始化失败
    offline,//下线
    changeOffline,//转接下线
    loginSuccess,//登录成功
    loginError,//登录失败
    kickOff,//被服务端踢出，账号重复登录
    serverACK,//服务器消息确认ack

    //进入队列成功状态
    onlineQueueSuccess,//上线不等待
    waitQueueSuccess,//上线且等待
    backUpQueueSuccess,//上线到backup队列

    offlineWaitQueue,//离开等待队列 准备进入到线上队列
    offlineBackUpQueue,//离开backup队列

    //进入队列失败状态
    onlineQueueError,//进入线上队列失败
    waitQueueError,//进入等待队列失败
    backUpQueueError//进入backup队列失败
}
