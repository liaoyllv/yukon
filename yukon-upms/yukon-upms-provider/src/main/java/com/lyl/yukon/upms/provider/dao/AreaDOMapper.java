package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.AreaDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AreaDO record);

    int insertSelective(AreaDO record);

    AreaDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AreaDO record);

    int updateByPrimaryKey(AreaDO record);

    /**
     * 查询市下的区
     * @param cityCode 市代码
     */
    List<AreaDO> selectByCityCode(@Param("cityCode") String cityCode);
}