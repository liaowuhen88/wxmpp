package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.AppCustomerFail;
import com.baodanyun.websocket.model.AppCustomerSuccess;
import com.baodanyun.websocket.model.AppCustomerSuccessExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppCustomerSuccessMapper {
    int countByExample(AppCustomerSuccessExample example);

    int deleteByExample(AppCustomerSuccessExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppCustomerSuccess record);

    int insertSelective(AppCustomerSuccess record);

    List<AppCustomerSuccess> selectByExample(AppCustomerSuccessExample example);

    AppCustomerSuccess selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppCustomerSuccess record, @Param("example") AppCustomerSuccessExample example);

    int updateByExample(@Param("record") AppCustomerSuccess record, @Param("example") AppCustomerSuccessExample example);

    int updateByPrimaryKeySelective(AppCustomerSuccess record);

    int updateByPrimaryKey(AppCustomerSuccess record);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertBatch(List<AppCustomerSuccess> list);
}