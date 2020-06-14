package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.ProvinceDO;

import java.util.List;

public interface ProvinceDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProvinceDO record);

    int insertSelective(ProvinceDO record);

    ProvinceDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProvinceDO record);

    int updateByPrimaryKey(ProvinceDO record);

    /**
     * 查询所有省列表
     */
    List<ProvinceDO> selectAll();

}