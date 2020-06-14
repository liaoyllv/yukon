package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrgParam extends BaseParam {
    /**
     * 机构id
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 负责人（展示信息，不存在绑定关系）
     */
    private String masterName;

    /**
     * 负责人电话
     */
    private String masterPhone;

    /**
     * 密码
     */
    private String password;

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

    /**
     * 菜单 id 列表
     */
    private List<String> menuIds;


}