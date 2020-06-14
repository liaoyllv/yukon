package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.entity.upms.DictDO;
import com.github.pagehelper.PageInfo;

/**
 * 字典service
 *
 * @author liaoyl
 */
public interface IDictService {


    /**
     * 新增字典
     *
     * @param dict 字典
     * @return T|F
     */
    boolean insertDict(DictDO dict);

    /**
     * 获取字典详情
     *
     * @param dictId 字典id
     * @return 字典
     */
    DictDO getDictById(String dictId);

    /**
     * 更新字典
     *
     * @param dict 字典
     * @return T|F
     */
    boolean updateDict(DictDO dict);

    /**
     * 删除字典
     *
     * @param dict 字典
     * @return T|F
     */
    boolean deleteDict(DictDO dict);

    /**
     * 获取字典分页
     *
     * @param pageNum  页号
     * @param pageSize 页大小
     * @param dictType 字典类型
     * @return 字典分页
     */
    PageInfo<DictDO> getDictPage(int pageNum, int pageSize, String dictType);

    /**
     * 校验字典名称是否存在
     *
     * @param dictId  字典id
     * @param dictKey 字典名称
     * @return T|F
     */
    boolean checkDictKey(String dictId, String dictKey);

    /**
     * 校验字典key是否存在
     *
     * @param dictId 字典id
     * @param value  字典
     * @return T|F
     */
    boolean checkDictValue(String dictId, String value);

}
