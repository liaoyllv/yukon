package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 负责人 id（这个机构的管理员）
     */
    private String masterId;
    /**
     * 简称
     */
    private String shortName;
    /**
     * 标语
     */
    private String slogan;
    /**
     * logo
     */
    private String logo;
    /**
     * 省
     */
    private String provinceCode;
    /**
     * 市
     */
    private String cityCode;
    /**
     * 区
     */
    private String areaCode;
    /**
     * 街道
     */
    private String streetCode;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 描述
     */
    private String description;

}