package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserOrgRelationDO extends BaseDO implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 组织架构id
     */
    private String orgId;

    /**
     * 是否切换访问该机构数据（登录运营平台显示的数据）：0-否，1-是
     */
    private String switchFlag;

    private static final long serialVersionUID = 1L;

}