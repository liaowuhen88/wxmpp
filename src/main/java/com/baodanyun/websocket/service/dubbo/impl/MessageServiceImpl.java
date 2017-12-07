package com.baodanyun.websocket.service.dubbo.impl;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.QuickReply;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.service.dubbo.MessageService;
import com.baodanyun.websocket.service.dubbo.bean.Message;
import com.baodanyun.websocket.util.Constant;
import com.baodanyun.websocket.util.DubboPostSendMsgUtils;
import com.baodanyun.websocket.util.DubboServiceConfig;
import com.baodanyun.websocket.util.JSONUtil;
import com.doubao.open.api.DefaultResponse;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaowuhen on 2017/12/1.
 */
@Service
public class MessageServiceImpl implements MessageService {
    protected static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Override
    public List<Message> getByMessage(Message query) throws BusinessException {
        if (null != query) {
            if (null == query.getReceiverid()) {
                throw new BusinessException("用户id不能为空");
            }
            if (null == query.getType()) {
                throw new BusinessException("类型type不能为空");
            }

            DefaultResponse response = DubboPostSendMsgUtils.send(DubboServiceConfig.SM_GetByMessage, JSON.toJSONString(query));

            if (response != null && response.getCode() == 200) {
                java.lang.reflect.Type userInfoType = new TypeToken<List<Message>>() {
                }.getType();
                return JSONUtil.fromJson(response.getContent(), userInfoType);
            } else {
                throw new BusinessException("dubbo 请求异常");
            }
        } else {
            throw new BusinessException("类型type不能为空");
        }
    }

    @Override
    public List<Message> getByMessage(Long uid, Integer type) throws BusinessException {
        Message query = new Message();
        query.setReceiverid(uid);
        query.setType(type);
        return getByMessage(query);
    }

    @Override
    public List<QuickReply> MessageToQuickReply(List<Message> list) {
        if (null != list) {
            List<QuickReply> qrs = new ArrayList<>();
            for (Message message : list) {
                QuickReply qr = new QuickReply();
                if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_PAY) {
                    qr.setTag("理赔进度通知-正常给付");
                    qr.setMessage("您的理赔已经结案，赔付金额xxx元，三个工作日内，您会收到理赔款，如果没有收到，一定联系豆包管家哦");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_ZLQQ) {
                    qr.setTag("理赔进度通知-资料齐全");
                    qr.setMessage("您的资料已齐全，我们会尽快为您处理。");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_WTJ) {
                    qr.setTag("理赔进度通知-问题件");
                    qr.setMessage("您的理赔存在问题，请按提示补充哦");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_CANCLE) {
                    qr.setTag("理赔进度通知-案件撤销");
                    qr.setMessage("很抱歉，您的案件已撤销，有问题可以联系豆包管家在线客服哦");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_SUBMIT) {
                    qr.setTag("理赔进度通知-提交成功");
                    qr.setMessage("小豆包已经收到您的理赔，我们会快马加鞭尽快审核哦。您还有什么疑问吗");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_BOUNCE) {
                    qr.setTag("理赔进度通知-拒付");
                    qr.setMessage("很抱歉，您的案件已拒付，如有疑问，请联系豆包管家在线客服哦。");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_YXQQ) {
                    qr.setTag("理赔进度通知-影像齐全-快递");
                    qr.setMessage("您的理赔已经超快速申请上线，请把资料快快交给人事，我们好帮您尽快理赔哦。");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_0) {
                    qr.setTag("理赔-案件作废");
                    qr.setMessage("很抱歉，您的案件已撤销，有问题可以联系豆包管家在线客服哦");
                } else if (message.getBizid() == Constant.MSG_BIZ_IN_CLA_101) {
                    qr.setTag("理赔-已受理-初审中");
                    qr.setMessage("豆包网已经收到您的理赔申请，想着多多关注后续信息哦。");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_REP) {
                    qr.setTag("体检报告生成后提醒");
                    qr.setMessage("您的体检报告已经出来喽，不想看看吗？");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_REP) {
                    qr.setTag("体检报告生成后提醒");
                    qr.setMessage("您的体检报告已经出来喽，不想看看吗？");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_DUE) {
                    qr.setTag("体检产品即将到期提醒");
                    qr.setMessage("您的体检马上就要到期了，还不预约吗？快快检查身体吧。");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_CAL) {
                    qr.setTag("取消体检预约");
                    qr.setMessage("这次的体检预约已经取消，安排好时间再来哦。");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_BOOK) {
                    qr.setTag("体检预约成功提醒");
                    qr.setMessage("您的体检已经预约成功，别忘记体检哦");
                } else if (message.getBizid() == Constant.MSG_BIZ_OTH_USER_BIND) {
                    qr.setTag("微信绑定通知");
                    qr.setMessage("您的豆包账户跟您的微信成功在一起喽。");
                } else if (message.getBizid() == Constant.MSG_BIZ_HE_PE_DUE) {
                    qr.setTag("体检时间即将到期提醒");
                    qr.setMessage("后天体检，别忘记哦。");
                }
                qrs.add(qr);
            }
            return qrs;
        } else {
            logger.info("list is null");
        }

        return null;
    }

}
