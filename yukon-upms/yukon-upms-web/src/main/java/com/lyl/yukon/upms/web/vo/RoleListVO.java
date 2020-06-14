package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.RoleDO;
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
public class RoleListVO {

    /**
     * 编号
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
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 角色列表VO
     *
     * @param roleList 原角色列表
     * @return list
     */
    public static List<RoleListVO> RoleListVo(List<RoleDO> roleList) {
        LinkedList<RoleListVO> listVos = new LinkedList<>();
        for (RoleDO role : roleList) {
            RoleListVO listVo = new RoleListVO();
            listVo.roleId = role.getId();
            BeanUtils.copyProperties(role, listVo);
            listVos.add(listVo);
        }
        return listVos;
    }
}
