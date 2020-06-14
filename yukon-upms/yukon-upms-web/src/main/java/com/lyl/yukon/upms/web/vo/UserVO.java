package com.lyl.yukon.upms.web.vo;

import com.lyl.yukon.common.entity.upms.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * <p>用户详情</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/18 15:50
 **/

@Data
public class UserVO {

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
     * 用户头像
     */
    private String avatar;

    /**
     * 性别：0-男，1-女
     */
    private String gender;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 描述
     */
    private String description;

    public UserVO(UserDO user) {
        this.userId = user.getId();
        BeanUtils.copyProperties(user, this);
    }

}
