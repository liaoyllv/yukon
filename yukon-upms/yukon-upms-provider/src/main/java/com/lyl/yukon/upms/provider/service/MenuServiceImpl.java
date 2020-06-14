package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.common.base.EnumMsgCode;
import com.lyl.yukon.common.exception.CCWException;
import com.lyl.yukon.common.entity.upms.MenuDO;
import com.lyl.yukon.common.entity.upms.OrgDO;
import com.lyl.yukon.upms.api.service.IMenuService;
import com.lyl.yukon.upms.api.dto.MenuTreeDTO;
import com.lyl.yukon.upms.provider.dao.MenuDOMapper;
import com.lyl.yukon.upms.provider.dao.OrgDOMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/17 16:12
 **/
@Service("menuService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class MenuServiceImpl implements IMenuService {

    private static Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuDOMapper menuDOMapper;
    @Autowired
    private OrgDOMapper orgDOMapper;

    /**
     * 递归生成
     *
     * @param result    id为parentId这层的列表
     * @param totalList 所有数据
     * @param parentId  父id
     * @return 最终的树结构列表
     */
    private static List<MenuTreeDTO> recursiveGen(List<MenuTreeDTO> result, List<MenuTreeDTO> totalList, String parentId) {
        for (MenuTreeDTO menu : totalList) {
            // 找到所有parentId的子菜单
            if (menu.getParentId().equals(parentId)) {
                result.add(menu);
                // 当前菜单的子菜单
                List<MenuTreeDTO> children = new LinkedList<>();
                // 填充这个菜单的子菜单
                recursiveGen(children, totalList, menu.getId());
                menu.setChildren(children);
            }
        }
        return result;
    }

    /**
     * 递归寻找当前菜单
     *
     * @param totalList 所有数据
     * @param menuId    要找的菜单id
     * @return 要找的菜单
     */
    private static MenuTreeDTO recursiveFind(List<MenuTreeDTO> totalList, String menuId) {
        for (MenuTreeDTO menu : totalList) {
            if (menu.getId().equals(menuId)) {
                return menu;
            } else {
                // 如果有子菜单
                if (!menu.getChildren().isEmpty()) {
                    return recursiveFind(menu.getChildren(), menuId);
                }
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean insertMenu(MenuDO menu) {
        // 排序值+1
        menu.setSort(menuDOMapper.selectMaxSort() + 1);
        return menuDOMapper.insertSelective(menu) > 0;
    }

    @Override
    public MenuDO getMenuById(String menuId) {
        return menuDOMapper.selectByPrimaryKey(menuId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean updateMenu(MenuDO menu) {
        return menuDOMapper.updateByPrimaryKeySelective(menu) > 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean deleteMenu(MenuDO menu) {
        // 如果有子菜单
        MenuTreeDTO menuTree = recursiveFind(getAllMenuTreeList(null), menu.getId());
        if (menuTree == null) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1006);
        }
        if (!menuTree.getChildren().isEmpty()) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1009);
        }
        // 删除此菜单
        if (menuDOMapper.updateByPrimaryKeySelective(menu) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    public List<MenuTreeDTO> getAllMenuTreeList(String ccFlag) {
        List<MenuTreeDTO> result = new LinkedList<>();
        recursiveGen(result, menuDOMapper.selectAll(ccFlag), "");
        return result;
    }

    @Override
    public boolean checkMenuName(String menuId, String menuName) {
        return menuDOMapper.checkMenuName(menuId, menuName) == null;
    }

    @Override
    public boolean checkMenuKey(String menuId, String menuKey) {
        return menuDOMapper.checkMenuKey(menuId, menuKey) == null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean move(String menuId, String direction) {
        MenuDO menu = menuDOMapper.selectByPrimaryKey(menuId);
        // 获取相邻的菜单
        MenuDO temp = menuDOMapper.selectAdjacentMenu(menu.getParentId(), menu.getId(), menu.getSort(), direction);
        if (temp == null) {
            return false;
        }
        // 互换sort
        Integer sort = menu.getSort();
        menu.setSort(temp.getSort());
        // 先把temp修改为一个临时值
        temp.setSort(-1);
        if (menuDOMapper.updateByPrimaryKeySelective(temp) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        if (menuDOMapper.updateByPrimaryKeySelective(menu) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        temp.setSort(sort);
        if (menuDOMapper.updateByPrimaryKeySelective(temp) < 1) {
            throw new CCWException(EnumMsgCode.MSG_SYSTEM_1005);
        }
        return true;
    }

    @Override
    public List<MenuDO> getByRoleId(String roleId) {
        return menuDOMapper.selectMenuListByRoleId(roleId);
    }

    @Override
    public List<MenuDO> getByUserId(String userId) {
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(userId);
        return menuDOMapper.selectMenuListByUserIdAndOrgId(userId, org.getId());
    }

    @Override
    public List<MenuDO> getFunctionListByMenuId(String menuId, String validFlag) {
        return menuDOMapper.selectFunctionListByMenuId(menuId, validFlag);
    }

    @Override
    public List<MenuTreeDTO> getMenuTreeByUserId(String userId) {
        OrgDO org = orgDOMapper.selectSwitchOnOrgByUserId(userId);
        List<MenuDO> menuDOList = menuDOMapper.selectMenuListByUserIdAndOrgId(userId, org.getId());
        List<MenuTreeDTO> result = new LinkedList<>();
        for (MenuDO menuDO : menuDOList) {
            MenuTreeDTO dto = new MenuTreeDTO();
            BeanUtils.copyProperties(menuDO, dto);
            result.add(dto);
        }
        recursiveGen(result, result, "");
        return result;
    }

}
