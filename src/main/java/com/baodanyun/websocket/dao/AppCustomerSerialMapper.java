package com.baodanyun.websocket.dao;

import com.baodanyun.websocket.model.AppCustomerSerial;
import com.baodanyun.websocket.model.AppCustomerSerialExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppCustomerSerialMapper {
    int countByExample(AppCustomerSerialExample example);

    int deleteByExample(AppCustomerSerialExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AppCustomerSerial record);

    int insertSelective(AppCustomerSerial record);

    List<AppCustomerSerial> selectByExample(AppCustomerSerialExample example);

    AppCustomerSerial selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppCustomerSerial record, @Param("example") AppCustomerSerialExample example);

    int updateByExample(@Param("record") AppCustomerSerial record, @Param("example") AppCustomerSerialExample example);

    int updateByPrimaryKeySelective(AppCustomerSerial record);

    int updateByPrimaryKey(AppCustomerSerial record);
}