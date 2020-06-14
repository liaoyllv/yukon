package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class OfficeParam extends BaseParam {
    /**
     * 编号
     */
    private String officeId;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 名称
     */
    private String officeName;

    /**
     * 类型：0-其他，1-机构，2-校区，3-部门
     */
    private String officeType;

    /**
     * 负责人（展示信息，不存在绑定关系）
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

    /**
     * 描述
     */
    private String description;

    /**
     * 合作模式：0-直营，1-合作，2-其他
     */
    private String coopType;

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


}