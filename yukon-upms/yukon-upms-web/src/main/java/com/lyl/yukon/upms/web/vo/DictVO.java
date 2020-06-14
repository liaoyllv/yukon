package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.DictDO;
import lombok.Data;

@Data
public class DictVO {
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
    private String dictValue;

    /**
     * 类型
     */
    private String dictType;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注信息
     */
    private String remarks;

    public DictVO(DictDO dict) {
        this.dictId = dict.getId();
        this.dictKey = dict.getDictKey();
        this.dictValue = dict.getDictValue();
        this.dictType = dict.getDictType();
        this.description = dict.getDescription();
        this.remarks = dict.getRemarks();
    }

}