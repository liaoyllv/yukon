package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.OrgDetailDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * <p>机构信息</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/8 10:50
 **/

@Data
public class OrgVO {

    /**
     * 编号
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 负责人名称
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

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
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 机构VO
     *
     * @param org 原机构
     */
    public OrgVO(OrgDetailDTO org) {
        BeanUtils.copyProperties(org, this);
        this.orgId = org.getId();
        this.orgName = org.getOfficeName();
    }
}
