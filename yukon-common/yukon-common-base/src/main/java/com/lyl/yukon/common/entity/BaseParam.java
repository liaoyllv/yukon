package com.lyl.yukon.common.entity;

import lombok.Data;

/**
 * <p>基础请求参数接收类</p>
 * 禁止添加业务字段
 *
 * @author liaoyl
 * @version 1.0 2019/3/18 10:20
 **/

@Data
public class BaseParam {

    /**
     * 页号
     */
    private Integer pageNum = 1;

    /**
     * 页大小
     */
    private Integer pageSize = 20;

    /**
     * 移动方向：0-向上，1-向下
     */
    private String direction;

    /**
     * 是否生效：0-不是，1-是
     */
    private String validFlag;

    /**
     * 是否显示失效：0-不显示，1-显示
     */
    private String displayFlag;
}

