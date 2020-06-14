package com.lyl.yukon.common.entity.upms;

import lombok.Data;

import java.io.Serializable;

@Data
public class StreetDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 街道代码
     */
    private String streetCode;
    /**
     * 父级区代码
     */
    private String areaCode;
    /**
     * 街道名称
     */
    private String streetName;
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