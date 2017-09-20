package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.bean.user.Visitor;
import com.baodanyun.websocket.bean.userInterface.Company;
import com.baodanyun.websocket.bean.userInterface.user.PersonalInfo;
import com.baodanyun.websocket.dao.ArchiveMessagesMapper;
import com.baodanyun.websocket.exception.BusinessException;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.PersonalService;
import com.baodanyun.websocket.service.QualityCheckService;
import com.baodanyun.websocket.service.UserServer;
import com.baodanyun.websocket.util.XMPPUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 质量检查
 */
@Service
public class QualityCheckServiceImpl implements QualityCheckService {
    private final Logger LOGGER = LoggerFactory.getLogger(QualityCheckServiceImpl.class);

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

        searchDto.setCustomerName(XMPPUtil.nameToJid(searchDto.getCustomerName()));
        searchDto.setStartTime(DateTime.parse(searchDto.getBeginDate(), format).getMillis());
        searchDto.setEndTime(DateTime.parse(searchDto.getEndDate(), format).getMillis());

        if (searchDto.getUserName() != null) {//用户名
            searchDto.setUserName(XMPPUtil.nameToJid(searchDto.getUserName()));
        }
    }

}
