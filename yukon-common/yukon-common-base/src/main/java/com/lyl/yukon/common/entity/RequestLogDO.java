package com.lyl.yukon.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>请求日志</p>
 *
 * @author liaoyl
 * @version 1.0 2019/04/18 10:10
 **/
@Data
public class RequestLogDO implements Serializable {
    private String id;

    /**
     * 接口地址
     */
    private String path;

    private String ip;

    /**
     * 请求参数
     */
    private String parameter;

    /**
     * 响应值
     */
    private String result;

    /**
     * 接口消耗时间
     */
    private long spendTime;

    /**
     * 操作人id
     */
    private String createId;

    private Date createDate;


}
