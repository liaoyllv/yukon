package com.lyl.yukon.upms.provider.dao;

import com.lyl.yukon.common.entity.upms.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDOMapper {
    int deleteByPrimaryKey(String id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    /**
     * 手机号查询
     *
     * @param phone 手机号
     * @return 用户信息
     */
    UserDO selectByUserMobile(@Param("phone") String phone);

    /**
     * 用户分页查询
     *
     * @param phone    手机号
     * @param userName 用户名
     * @return 用户列表
     */
    List<UserDO> selectUserPage(@Param("phone") String phone, @Param("userName") String userName);


    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     */
    UserDO selectByPhone(@Param("phone") String phone);
}