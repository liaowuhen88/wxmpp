package com.baodanyun.websocket.service.impl;

import com.alibaba.fastjson.JSON;
import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.qualitycheck.Qualitycheck;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.bean.userInterface.Company;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.dao.ArchiveMessagesMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.PersonalService;
import com.baodanyun.websocket.service.QualityCheckService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.CommonConfig;
import com.baodanyun.websocket.util.XMPPUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质量检查
 */
@Service
public class QualityCheckServiceImpl implements QualityCheckService {
    /*mongoDB事件统计表集合名*/
    private static final String COLLECTIO_NNAME = "userEventList";
    /*留言都是给maqiumeng*/
    private static final String LEAVE_MSG_CUSTOMER = "maqiumeng@126xmpp";
    private final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);
    @Autowired
    public MongoTemplate mongoTemplate;
    @Autowired
    private ArchiveMessagesMapper archiveMessagesMapper;
    @Autowired
    private UserServer userServer;
    @Autowired
    private PersonalService personalService;

    @Override
    public List<String> findAllGuestName(QualitySearchDto searchDto) {
        this.setSearchDtoProp(searchDto);

        return archiveMessagesMapper.findAllGuest(searchDto);
    }

    @Override
    public List<HistoryMsg> loadChatMsgFromUser(QualitySearchDto searchDto) {
        this.setSearchDtoProp(searchDto);
        return archiveMessagesMapper.loadChatMsgFromUser(searchDto);
    }

    @Override
    public List<PersonalInfo> findUserList(List<String> phoneList) {
        List<PersonalInfo> personalInfos = new ArrayList<>();
        try {
            for (String phone : phoneList) {
                if (phone.matches("^\\d{11}$")) {//电话号码
                    Visitor visitor = userServer.initByPhone(phone);
                    if (visitor != null) {
                        PersonalInfo person = personalService.getPersonalInfo(visitor.getUid());
                        List<Company> companies = personalService.getCompany(visitor.getUid());
                        if (!CollectionUtils.isEmpty(companies)) {
                            person.setAttr(companies.get(0).getEname()); //公司名
                        }
                        personalInfos.add(person);
                    }
                } else {//微信open_id
                    PersonalInfo personalInfo = new PersonalInfo();
                    personalInfo.setPname(phone); //openid当姓名
                    personalInfo.setMobile(phone);
                    personalInfos.add(personalInfo);
                }
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }

        return personalInfos;
    }

    /**
     * 设置条件查询时间缀
     *
     * @param searchDto
     */
    private void setSearchDtoProp(QualitySearchDto searchDto) {
        final DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        searchDto.setStartTime(DateTime.parse(searchDto.getBeginDate(), format).getMillis());
        searchDto.setEndTime(DateTime.parse(searchDto.getEndDate(), format).getMillis());

        if (StringUtils.isNotBlank(searchDto.getUserName())) {//用户名
            searchDto.setUserName(XMPPUtil.nameToJid(searchDto.getUserName()));
        }
    }

    @Override
    public AggregationResults<Map> getEventResult(String evtCode, QualitySearchDto searchDto) {
        if (StringUtils.isBlank(evtCode)) {
            return null;
        }
        this.setSearchDtoProp(searchDto);
        LOGGER.info("搜索条件: evtcode={},{}", evtCode, JSON.toJSONString(searchDto));

        Criteria c = Criteria.where("evt")
                .is(evtCode).and("t")
                .gte(searchDto.getStartTime())
                .lte(searchDto.getEndTime());

        if (StringUtils.isNotBlank(searchDto.getCustomerName())) {//客服
            c.and("oid").is(searchDto.getCustomerName());
        }

        Criteria criteria = Criteria.where("array").elemMatch(c);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );

        aggregation.group("userid").count().as("total");
        AggregationResults<Map> aggregate = mongoTemplate.aggregate(aggregation, COLLECTIO_NNAME, Map.class);
        return aggregate;
    }

    private int getSize(final String evtCode, QualitySearchDto searchDto) {
        int size = this.getEventResult(evtCode, searchDto).getMappedResults().size();
        return size;
    }

    @Override
    public Map<String, Integer> getEventTotalMap(QualitySearchDto searchDto) {
        Map<String, Integer> map = new HashMap<>();

        String leave = searchDto.getCustomerName();
        if (LEAVE_MSG_CUSTOMER.equals(searchDto.getCustomerName())
                || StringUtils.isBlank(searchDto.getCustomerName())) {//全部或者是马秋萌时统计留言数量不要array.oid条件
            searchDto.setCustomerName(null);
            map.put("leaveCount", this.getSize(CommonConfig.MSG_BIZ_KF_LEAVE_MESSAGE, searchDto));
        } else {
            map.put("leaveCount", 0);
        }

        searchDto.setCustomerName(leave);
        map.put("wxActiveCount", this.getSize(CommonConfig.MSG_SOURCE_WE_CHAT_ACTIVE, searchDto));
        map.put("h5Count", this.getSize(CommonConfig.MSG_SOURCE_H5, searchDto));
        map.put("wxPassiveCount", this.getSize(CommonConfig.MSG_SOURCE_WE_CHAT_PASSIVE, searchDto));
        map.put("enterCount", this.getSize(CommonConfig.MSG_BIZ_KF_ENTER, searchDto));

        LOGGER.info("统计数量: {}", JSON.toJSONString(map));

        return map;
    }

    @Override
    public List<PersonalInfo> findMongoEvtData(int code, QualitySearchDto searchDto) {
        String evtCode = this.getEvtCode(code);
        if (LEAVE_MSG_CUSTOMER.equals(searchDto.getCustomerName())
                && evtCode.equals(CommonConfig.MSG_BIZ_KF_LEAVE_MESSAGE)) {
            searchDto.setCustomerName(null);
        }

        AggregationResults<Map> aggregate = this.getEventResult(evtCode, searchDto);
        List<Map> list = (List) aggregate.getMappedResults();

        List<Qualitycheck> userList = new ArrayList<>();
        for (int i = 0, len = list.size(); i < len; i++) {
            Map map = list.get(i);
            Qualitycheck qualitycheck = JSON.parseObject(JSON.toJSONString(map), Qualitycheck.class);
            LOGGER.info("mongodata: {}", JSON.toJSONString(qualitycheck));
            userList.add(qualitycheck);
        }

        return this.buildPersoninfoList(userList);
    }


    public List<PersonalInfo> buildPersoninfoList(List<Qualitycheck> list) {
        List<PersonalInfo> personalInfos = new ArrayList<>();
        try {
            for (Qualitycheck evtInfo : list) {
                String uid = evtInfo.getUserid();
                String openid = evtInfo.getOpenid();
                LOGGER.info("用户uid={};openid: {}", uid, openid);

                if (uid.matches("^\\d+$")) {
                    long userID = Long.parseLong(uid);
                    PersonalInfo person = personalService.getPersonalInfo(userID);
                    if (person == null)
                        continue;

                    List<Company> companies = personalService.getCompany(userID);
                    if (!CollectionUtils.isEmpty(companies)) {
                        person.setAttr(companies.get(0).getEname()); //公司名
                    }

                    personalInfos.add(person);
                } else {//微信open_id
                    PersonalInfo personalInfo = new PersonalInfo();
                    personalInfo.setPname(uid); //openid当姓名
                    personalInfo.setMobile(uid);
                    personalInfo.setUseraccountid(0L);

                    personalInfos.add(personalInfo);
                }
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getMessage());
            return null;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return null;
        }

        return personalInfos;
    }

    private String getEvtCode(int code) {
        String evtCode = "";
        switch (code) {
            case 1:
                evtCode = CommonConfig.MSG_BIZ_KF_LEAVE_MESSAGE;
                break;
            case 2:
                evtCode = CommonConfig.MSG_SOURCE_WE_CHAT_ACTIVE;
                break;
            case 3:
                evtCode = CommonConfig.MSG_SOURCE_H5;
                break;
            case 4:
                evtCode = CommonConfig.MSG_SOURCE_WE_CHAT_PASSIVE;
                break;
            case 5:
                evtCode = CommonConfig.MSG_BIZ_KF_ENTER;
                break;
            default:
                break;
        }

        return evtCode;
    }
}
