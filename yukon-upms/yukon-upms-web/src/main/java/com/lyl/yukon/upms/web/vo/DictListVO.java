package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.DictDO;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class DictListVO {
    /**
     * 编号
     */
    private String dictId;

    /**
     * 字典key
     */
    private String dictKey;

    /**
     * 数据值
     */
    private String value;

    /**
     * 类型
     */
    private String dictType;

    /**
     * 描述
     */
    private String description;

    public DictListVO(DictDO dict) {
        this.dictId = dict.getId();
        this.dictKey = dict.getDictKey();
        this.value = dict.getDictValue();
        this.dictType = dict.getDictType();
        this.description = dict.getDescription();
    }

    /**
     * 获取字典列表
     *
     * @param dictList 原字典列表数据
     */
    public static List<DictListVO> DictListVo(List<DictDO> dictList) {
        LinkedList<DictListVO> result = new LinkedList<>();
        for (DictDO dict : dictList) {
            result.add(new DictListVO(dict));
        }
        return result;
    }


}