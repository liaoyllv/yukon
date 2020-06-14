package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.CityDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CityDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CityDO record);

    int insertSelective(CityDO record);

    CityDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CityDO record);

    int updateByPrimaryKey(CityDO record);

    /**
     * 查询省下的市
     *
     * @param provinceCode 省代码
     */
    List<CityDO> selectByProvinceCode(@Param("provinceCode") String provinceCode);
}