package com.lyl.yukon.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p>json web token</p>
 *
 * @author liaoyl
 * @version 1.0 2019/07/19 15:41
 **/
@Data
public class TokenInfo {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 终端类型
     */
    private String terminalType;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建的IP
     */
    private String ip;

}
