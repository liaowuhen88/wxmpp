package com.baodanyun.websocket.bean.conf;

/**
 * Created by yutao on 2016/10/8.
 * 客服的自定义配置
 */
public class CustomerConf extends Conf{
    //定义线上队列的默认大小
    private int onlineQueueDefaultSize = 30;
    //定义等待队列的默认大小
    private int waitQueueDefaultSize = 1000;

    //TODO 休息日 ，欢迎语设置


    public int getOnlineQueueDefaultSize() {
        return onlineQueueDefaultSize;
    }

    public void setOnlineQueueDefaultSize(int onlineQueueDefaultSize) {
        this.onlineQueueDefaultSize = onlineQueueDefaultSize;
    }

    public int getWaitQueueDefaultSize() {
        return waitQueueDefaultSize;
    }

    public void setWaitQueueDefaultSize(int waitQueueDefaultSize) {
        this.waitQueueDefaultSize = waitQueueDefaultSize;
    }
}
