package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.StreetDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StreetDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StreetDO record);

    int insertSelective(StreetDO record);

    StreetDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StreetDO record);

    int updateByPrimaryKey(StreetDO record);

    /**
     * 查询区下的街道
     *
     * @param areaCode 区代码
     */
    List<StreetDO> selectByAreaCode(@Param("areaCode") String areaCode);
}