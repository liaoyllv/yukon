package com.lyl.yukon.upms.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>角色 id、名称</p>
 *
 * @author liaoyl
 * @version 1.0 2019/08/12 14:04
 **/
@Data
public class RoleBriefDTO implements Serializable {

    /**
     * 角色id
     */
    private String id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 访问的校区 id 列表
     */
    private List<String> schoolIds;
    /**
     * 访问的菜单 id 列表
     */
    private List<String> menuIds;
}
