package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.MsgLogDO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MsgLogMapper {

    int deleteByPrimaryKey(String msgId);

    int insert(MsgLogDO record);

    int insertSelective(MsgLogDO record);

    MsgLogDO selectByPrimaryKey(String msgId);

    int updateByPrimaryKeySelective(MsgLogDO record);

    int updateByPrimaryKeyWithBLOBs(MsgLogDO record);

    int updateByPrimaryKey(MsgLogDO record);

    List<MsgLogDO> selectTimeoutMsg();

    int updateStatus(@Param("msgId") String msgId,@Param("status") int status);

    int updateTryCount(String msgId, Date nextTryTime);
}