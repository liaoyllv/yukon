package com.lyl.yukon.upms.api.service;


import com.lyl.yukon.common.entity.upms.UserDO;
import com.github.pagehelper.PageInfo;

import java.io.IOException;
import java.util.List;

/**
 * 用户service
 *
 * @author liaoyl
 */
public interface IUserService {

    /**
     * 获取用户
     */
    UserDO getUserById(String userId);

    /**
     * 手机号获取用户信息
     */
    UserDO getUserByPhone(String phone);

    /**
     * 修改密码
     *
     * @param mobile   手机号
     * @param username 用户名
     * @param password 密码
     * @param vCode    验证码
     * @return T|F
     */
    boolean modifyPassword(String mobile, String username, String password, String vCode) throws IOException;

    /**
     * 用户分页
     *
     * @param userMobile 手机号
     * @param userName   用户名
     * @param pageNum    页号
     * @param pageSize   页大小
     * @return 用户分页对象
     */
    PageInfo<UserDO> getUserPage(String userMobile, String userName, int pageNum, int pageSize);

    /**
     * 新增用户
     *
     * @param user      用户信息
     * @param officeIds 组织架构 id
     * @param roleIds   角色 id
     * @param schoolIds 校区 id
     * @param schoolIds 菜单 id
     * @return T|F
     */
    boolean insert(UserDO user, List<String> officeIds, List<String> roleIds, List<String> schoolIds, List<String> menuIds);

    /**
     * 更新用户
     *
     * @param user      用户信息
     * @param officeIds 组织架构 id
     * @param roleIds   角色 id
     * @param schoolIds 校区 id
     * @param schoolIds 菜单 id
     * @return T|F
     */
    boolean update(UserDO user, List<String> officeIds, List<String> roleIds, List<String> schoolIds, List<String> menuIds);

    /**
     * 更新用户基本信息
     *
     * @param user 用户信息
     * @return T|F
     */
    boolean update(UserDO user);

    /**
     * 手机号是否存在
     *
     * @param phone 手机号
     * @return T|F
     */
    boolean isPhoneExist(String phone);

    /**
     * 是否是 admin
     *
     * @param userId 用户 id
     * @return T|F
     */
    boolean isAdmin(String userId);
}
