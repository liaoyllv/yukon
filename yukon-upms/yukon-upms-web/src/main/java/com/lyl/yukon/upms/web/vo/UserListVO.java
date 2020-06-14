package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>用户列表</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/18 15:50
 **/
@Data
public class UserListVO {

    /**
     * 编号
     */
    private String userId;

    /**
     * 手机
     */
    private String phone;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别：0-男，1-女
     */
    private String gender;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 所在部门
     */
    private String officeName;

    public static List<UserListVO> UserListVo(List<UserDO> userList) {
        LinkedList<UserListVO> listVos = new LinkedList<>();
        for (UserDO user : userList) {
            UserListVO listVo = new UserListVO();
            listVo.userId = user.getId();
            BeanUtils.copyProperties(user, listVo);
            listVos.add(listVo);
        }
        return listVos;
    }
}
