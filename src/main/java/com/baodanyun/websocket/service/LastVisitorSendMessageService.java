package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.LastSendMessage;
import com.baodanyun.websocket.util.DateUtils;
import com.baodanyun.websocket.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liaowuhen on 2016/11/15.
 */
@Service
public class LastVisitorSendMessageService {
    // 保存访客最后一次发送消息的时间戳
    public static final int ninater = 5;
    public static final long delayTime = 1000 * 60 * ninater;
    public static final String toJid = PropertiesUtil.get(LastVisitorSendMessageService.class.getClassLoader(), "config.properties").get("control-id");

    public final static Map<String, LastSendMessage> map = new ConcurrentHashMap<>();

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransferServer transferServer;

    @Autowired
    private UserCacheServer userCacheServer;

    /*public void search() {
        if (!CollectionUtils.isEmpty(map)) {
            Set<Map.Entry<String, LastSendMessage>> set = map.entrySet();
            Iterator<Map.Entry<String, LastSendMessage>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String, LastSendMessage> ent = it.next();
                long now = System.currentTimeMillis();
                long ti = now - ent.getValue().getTime();
                if (ti > delayTime) {
                    logger.info("用户" + ent.getKey() + "------->" + DateUtils.unixTimestampToDate(ent.getValue().getTime()) + "消息已超时未处理，超时时间:" + (now - ent.getValue().getTime()) / 1000 + "秒");
                    //Customer customer = (Customer) httpServletRequest.getSession().getAttribute(Common.CUSTOMER_USER_KEY);
                    try {
                        Transferlog tm = new Transferlog();
                        tm.setTransferto(toJid);
                        tm.setTransferfrom(ent.getValue().getTo());
                        tm.setVisitorjid(ent.getValue().getFrom());
                        tm.setCause("超时转接");
                        Visitor visitor = userCacheServer.getUserVisitor(ent.getValue().getTo());
                        boolean flag = transferServer.changeVisitorTo(tm, visitor);
                        if (flag) {
                            logger.info("from [" + ent.getValue().getTo() + "]转接[" + ent.getValue().getFrom() + "]+ to [" + toJid + "]成功");
                        } else {
                            logger.info("from [" + ent.getValue().getTo() + "]转接[" + ent.getValue().getFrom() + "]+ to [" + toJid + "]失败");
                        }
                        it.remove();
                    } catch (Exception e) {
                        logger.error("转接异常", e);
                        it.remove();
                    }
                } else {
                    logger.info("用户" + ent.getKey() + "------->" + DateUtils.unixTimestampToDate(ent.getValue().getTime()) + "已延迟时间:" + (now - ent.getValue().getTime()) / 1000 + "秒");
                }

            }
        } else {
            logger.info("map is null");

        }
    }*/

    public void add(String jid, String cid) {
        LastSendMessage la = new LastSendMessage();
        la.setFrom(jid);
        la.setTo(cid);
        la.setTime(System.currentTimeMillis());
        map.put(jid, la);
        logger.info(cid + "接收到访客[" + jid + "]端消息,时间为:" + DateUtils.getNowTime());

    }

    public LastSendMessage get(String jid) {
        return map.get(jid);
    }


    public void remove(String jid, String cid) {
        logger.info(cid + "回复访客端消息,时间为:" + DateUtils.getNowTime());
        map.remove(jid);
    }
}
