package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictDO extends BaseDO implements Serializable {

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

    private static final long serialVersionUID = 1L;

}