package com.lyl.yukon.common.entity.upms;

import com.lyl.yukon.common.entity.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserSchoolRelationDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 用户 id
     */
    private String userId;
    /**
     * 校区 id
     */
    private String schoolId;

}