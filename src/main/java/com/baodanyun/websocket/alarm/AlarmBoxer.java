package com.baodanyun.websocket.alarm;

import com.baodanyun.websocket.enums.AlarmEnum;
import com.baodanyun.websocket.event.AlarmEvent;
import com.baodanyun.websocket.util.DateUtils;
import com.baodanyun.websocket.util.XMPPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 告警容器
 * @author hubo
 * @since 2017-06-29 17:58
 **/
public class AlarmBoxer {
    protected final static Logger LOGGER = LoggerFactory.getLogger(AlarmBoxer.class);

    private final static AlarmBoxer alarmBoxer = new AlarmBoxer();

    private final Map<String, AlarmEvent> alarmMap = new ConcurrentHashMap();

    public static synchronized AlarmBoxer getInstance() {
        return alarmBoxer;
    }

    public synchronized void put(AlarmEvent alarmEvent) {
        String visitorJid = XMPPUtil.jidToName(alarmEvent.getMessage().getFrom());
        alarmMap.put(visitorJid, alarmEvent);

        LOGGER.info("添加任务: key=" + visitorJid);
    }

    public synchronized void remove(AlarmEvent alarmEvent) {
        String key;
        if (alarmEvent.getAlarmEnum() == AlarmEnum.VISITOR) {
            key = alarmEvent.getMessage().getFrom();
        } else {
            key = alarmEvent.getMessage().getTo();
        }

        key = XMPPUtil.jidToName(key);
        if (alarmMap.containsKey(key)) {
            alarmMap.remove(key);

            LOGGER.info("删除任务: key=" + key);
        }
    }

    /**
     * 定时扫描是否map中有数据且超时
     * <ol>
     * <li>超时5分钟则告警客服</li>
     * <li>15分钟则告警到Boss</li>
     * <li>30分钟后清除</li>
     * <li>每告警一次数据记录入库</li>
     * </ol>
     */
    public synchronized void doAlarmJob() {
        LOGGER.info(String.format("容器map任务条数%s", alarmMap.size()));

        for (Map.Entry<String, AlarmEvent> map : alarmMap.entrySet()) {

            AlarmEvent alarmInfo = map.getValue();
            long ruleTime = DateUtils.getMinutesDiff(alarmInfo.getVisitorSendMsgTime());//时间差

            //组合责任链
            AlarmToDestroy destroy = new AlarmToDestroy.Builder().build();
            AlarmToBoss boss = new AlarmToBoss.Builder().nextHandler(destroy).build();
            AlarmToCustomer customer = new AlarmToCustomer.Builder().nextHandler(boss).build();

            customer.alarm(ruleTime, alarmInfo); //告警
        }
    }

}
