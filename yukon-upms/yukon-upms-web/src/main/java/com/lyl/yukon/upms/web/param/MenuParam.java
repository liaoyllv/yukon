package com.lyl.yukon.upms.web.param;

import com.lyl.yukon.common.entity.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>菜单请求参数接收</p>
 *
 * @author liaoyl
 * @version 1.0 2019/03/15 15:02
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuParam extends BaseParam {

    /**
     * 菜单id
     */
    private String menuId;

    /**
     * 父级编号
     */
    private String parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单key值
     */
    private String menuKey;

    /**
     * 链接
     */
    private String href;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单类型：0-页面，1-功能
     */
    private String menuType;

    /**
     * 是否是 CC 内部使用：0-不是，1-是
     */
    private String ccFlag;

}
