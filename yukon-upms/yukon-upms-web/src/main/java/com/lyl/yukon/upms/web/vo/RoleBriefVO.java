package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.upms.api.dto.RoleBriefDTO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>角色列表</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/18 15:50
 **/

@Data
public class RoleBriefVO {

    /**
     * 角色 id
     */
    private String roleId;

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

    /**
     * 角色列表VO
     *
     * @param roleList 原角色列表
     * @return list
     */
    public static List<RoleBriefVO> RoleListVo(List<RoleBriefDTO> roleList) {
        LinkedList<RoleBriefVO> listVos = new LinkedList<>();
        for (RoleBriefDTO role : roleList) {
            RoleBriefVO listVo = new RoleBriefVO();
            listVo.roleId = role.getId();
            BeanUtils.copyProperties(role, listVo);
            listVos.add(listVo);
        }
        return listVos;
    }
}
