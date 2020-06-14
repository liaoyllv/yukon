package com.lyl.yukon.common.entity.upms;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 市代码
     */
    private String cityCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 省代码
     */
    private String provinceCode;
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