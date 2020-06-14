package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegionParam extends BaseParam {
    /**
     * 区域编码
     */
    private String regionCode;

}