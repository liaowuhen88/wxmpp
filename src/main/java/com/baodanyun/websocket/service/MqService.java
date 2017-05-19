package com.baodanyun.websocket.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;

/**
 * Created by admin on 2015/11/17.
 */
@Service
public class MqService {

    protected Log logger = LogFactory.getLog(MqService.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 向指定队列发送消息
     */
    public void sendTextMsg(Destination destination, final String msg) {
        logger.info("向默认队列"+destination.toString()+"发送文本消息:"+msg);
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

    /**
     * 向指定队列发送消息
     */
    public boolean sendObjMsg(Destination destination, final Serializable obj) {
        logger.info("向默认队列"+destination.toString()+"发送OBJ消息:"+obj.toString());
        try {
            jmsTemplate.send(destination, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    return session.createObjectMessage(obj);
                }
            });
        }catch (JmsException e){
            //TODO 主备
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
