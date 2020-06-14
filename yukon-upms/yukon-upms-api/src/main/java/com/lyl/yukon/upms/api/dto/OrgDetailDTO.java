package com.lyl.yukon.upms.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>机构列表 DTO</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/08 09:10
 **/
@Data
public class OrgDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 机构 id
     */
    private String id;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 街道
     */
    private String street;
    /**
     * 详细地址
     */
    private String address;

    /**
     * 描述
     */
    private String description;

    /**
     * 组织架构名称
     */
    private String officeName;

    /**
     * 负责人id
     */
    private String masterId;

    /**
     * 负责人
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

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


}
