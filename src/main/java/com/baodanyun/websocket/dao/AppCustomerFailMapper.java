package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.AppCustomerFail;
import com.baodanyun.websocket.model.AppCustomerFailExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppCustomerFailMapper {
    int countByExample(AppCustomerFailExample example);

    int deleteByExample(AppCustomerFailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppCustomerFail record);

    int insertSelective(AppCustomerFail record);

    List<AppCustomerFail> selectByExample(AppCustomerFailExample example);

    AppCustomerFail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppCustomerFail record, @Param("example") AppCustomerFailExample example);

    int updateByExample(@Param("record") AppCustomerFail record, @Param("example") AppCustomerFailExample example);

    int updateByPrimaryKeySelective(AppCustomerFail record);

    int updateByPrimaryKey(AppCustomerFail record);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertBatch(List<AppCustomerFail> list);

    /**
     * 根据批次号统计记录数
     *
     * @param serialNo 批次号
     * @return 总数
     */
    int countBySerialNo(String serialNo);
}