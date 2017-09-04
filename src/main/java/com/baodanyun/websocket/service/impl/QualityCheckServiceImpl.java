package com.baodanyun.websocket.service.impl;

import com.baodanyun.websocket.bean.msg.HistoryMsg;
import com.baodanyun.websocket.dao.ArchiveMessagesMapper;
import com.baodanyun.websocket.quality.dto.QualitySearchDto;
import com.baodanyun.websocket.service.QualityCheckService;
import com.baodanyun.websocket.util.XMPPUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 质量检查
 */
@Service
public class QualityCheckServiceImpl implements QualityCheckService {


    @Autowired
    private ArchiveMessagesMapper archiveMessagesMapper;

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
