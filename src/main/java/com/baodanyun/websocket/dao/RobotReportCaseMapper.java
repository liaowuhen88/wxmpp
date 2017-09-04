package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.RobotReportCase;
import com.baodanyun.websocket.model.RobotReportCaseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RobotReportCaseMapper {
    int countByExample(RobotReportCaseExample example);

    int deleteByExample(RobotReportCaseExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RobotReportCase record);

    int insertSelective(RobotReportCase record);

    List<RobotReportCase> selectByExample(RobotReportCaseExample example);

    RobotReportCase selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RobotReportCase record, @Param("example") RobotReportCaseExample example);

    int updateByExample(@Param("record") RobotReportCase record, @Param("example") RobotReportCaseExample example);

    int updateByPrimaryKeySelective(RobotReportCase record);

    int updateByPrimaryKey(RobotReportCase record);

    List<RobotReportCase> findNotFinishData();

    /**
     * 按时间倒序去重查询uid的所有批次号
     *
     * @param uid
     * @return
     */
    List<RobotReportCase> findSerialNumberList(Long uid);

    /**
     * 根据电话号码查找
     *
     * @param phone
     * @return
     */
    List<RobotReportCase> findRobotListByPhone(Long phone);
}