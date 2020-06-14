package com.lyl.yukon.common.entity.upms;

import lombok.Data;

import java.io.Serializable;

@Data
public class AreaDO implements Serializable {
    private Integer id;

    /**
     * 区代码
     */
    private String areaCode;

    /**
     * 父级市代码
     */
    private String cityCode;

    /**
     * 区名称
     */
    private String areaName;

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

    private static final long serialVersionUID = 1L;

}