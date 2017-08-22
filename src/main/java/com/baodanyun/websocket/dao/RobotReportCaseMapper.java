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

    List<String> findNotFinishData();
}