package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.DictDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(DictDO record);

    int insertSelective(DictDO record);

    DictDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DictDO record);

    int updateByPrimaryKey(DictDO record);

    /**
     * 字典分页
     *
     * @param dictType 字典类型
     * @return 列表
     */
    List<DictDO> selectPage(@Param("dictType") String dictType);

    /**
     * 查询除开id为xx以外key为xx的数据，
     *
     * @param id      字典id
     * @param dictKey 字典key
     * @return 字典
     */
    DictDO checkDictKey(@Param("id") String id, @Param("dictKey") String dictKey);

    /**
     * 查询除开id为xx以外value为xx的数据，
     *
     * @param id    字典id
     * @param value 字典value
     * @return 字典
     */
    DictDO checkDictValue(@Param("id") String id, @Param("dictValue") String value);
}