package com.baodanyun.websocket.service;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.dao.ArchiveMessagesMapper;
import com.baodanyun.websocket.model.HistoryMessageModel;
import com.baodanyun.websocket.util.JSONUtil;
import com.baodanyun.websocket.util.XmllUtil;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by liaowuhen on 2016/11/23.
 */
@Service
public class ArchiveMessagesServer {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ArchiveMessagesMapper archiveMessagesMapper;

    public List<HistoryMsg> selectByFromAndTo(HistoryMessageModel model) {
        return archiveMessagesMapper.selectByFromAndTo(model);
    }

    public void getHistoryMsgList(List<HistoryMsg> li) {

        if (!CollectionUtils.isEmpty(li)) {
            Iterator<HistoryMsg> it = li.iterator();
            while (it.hasNext()) {
                HistoryMsg me = it.next();

                try {
                    if (null == me) {
                        return;
                    }

                    if (null != me.getFromCard()) {
                        List<Element> els = XmllUtil.xmlElements(me.getFromCard());
                        if (!CollectionUtils.isEmpty(els)) {
                            Iterator<Element> eit = els.iterator();
                            while (eit.hasNext()) {
                                Element e = eit.next();
                                if (e.getName().equals("key")) {
                                    String json = e.getText();
                                    Visitor u = JSONUtil.toObject(Visitor.class, json);
                                    me.setFromName(u.getNickName());
                                    me.setIcon(u.getIcon());
                                }
                            }
                        }
                    }

                    if (null != me.getToCard()) {
                        List<Element> elsto = XmllUtil.xmlElements(me.getToCard());
                        if (!CollectionUtils.isEmpty(elsto)) {
                            Iterator<Element> eit = elsto.iterator();
                            while (eit.hasNext()) {
                                Element e = eit.next();
                                if (e.getName().equals("key")) {
                                    String json = e.getText();
                                    Visitor u = JSONUtil.toObject(Visitor.class, json);
                                    me.setToName(u.getNickName());
                                    me.setToIcon(u.getIcon());
                                }
                            }
                        }
                    }


                } catch (Exception e) {
                    logger.error("vcard 解析异常", e);
                }

            }
        }
    }

}
