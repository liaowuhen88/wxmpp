package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.AppCustomer;
import com.baodanyun.websocket.model.AppCustomerExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AppCustomerMapper {
    int countByExample(AppCustomerExample example);

    int deleteByExample(AppCustomerExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppCustomer record);

    int insertSelective(AppCustomer record);

    List<AppCustomer> selectByExample(AppCustomerExample example);

    AppCustomer selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppCustomer record, @Param("example") AppCustomerExample example);

    int updateByExample(@Param("record") AppCustomer record, @Param("example") AppCustomerExample example);

    int updateByPrimaryKeySelective(AppCustomer record);

    int updateByPrimaryKey(AppCustomer record);
}