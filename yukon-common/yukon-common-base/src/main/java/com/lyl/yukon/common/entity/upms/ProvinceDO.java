package com.lyl.yukon.common.entity.upms;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProvinceDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 省代码
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 简称
     */
    private String shortName;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    private Integer sort;
    private String remarks;

}