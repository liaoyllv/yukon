package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DictParam extends BaseParam {
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

}