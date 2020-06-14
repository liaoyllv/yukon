package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.OfficeDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class OfficeVO {
    /**
     * id
     */
    private String officeId;

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
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 描述
     */
    private String description;

    /**
     * 父节点名称
     */
    private String parentOfficeName;

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

    public OfficeVO(OfficeDO office, String parentOrgName) {
        this.officeId = office.getId();
        this.parentOfficeName = parentOrgName;
        BeanUtils.copyProperties(office, this);
    }

}