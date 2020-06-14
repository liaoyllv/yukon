package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>角色请求参数接收</p>
 *
 * @author liaoyl
 * @version 1.0 2019/03/15 15:02
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class RoleParam extends BaseParam {

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述
     */
    private String description;

    /**
     * 拥有的菜单id列表
     */
    private List<String> menuIds;

    /**
     * 查看的校区id列表
     */
    private List<String> schoolIds;

    /**
     * 机构id
     */
    private String orgId;

}
