package com.lyl.yukon.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class MsgLogDO implements Serializable {
    private String msgId;

    private String exchange;

    private String routingKey;

    private Integer status;

    private Integer tryCount;

    private Date nextTryTime;

    private Date createTime;

    private Date updateTime;

    private String msg;

    private static final long serialVersionUID = 1L;


}