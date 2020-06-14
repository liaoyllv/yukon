package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserOfficeRelationDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 组织架构 id
     */
    private String officeId;

}