package com.lyl.yukon.common.entity;

import com.lyl.yukon.common.constant.SystemConstant;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

/**
 * 基础 DO，方便插入更新
 */

@Data
public class BaseDO {
    /**
     * 主键 ID
     */
    private String id;

    /**
     * 创建人 id
     */
    private String createId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人 id
     */
    private String updateId;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 删除标记：0-未删除，1-已删除
     */
    private String delFlag;

    /**
     * 插入数据前执行
     *
     * @param userId 操作人 id
     */
    public void preInsert(String userId) {
        this.id = UUID.randomUUID().toString();
        this.createId = userId;
        this.updateId = userId;
        this.createDate = new Date();
        this.updateDate = this.createDate;
        this.delFlag = SystemConstant.DEL_FLAG_NO;
    }

    /**
     * 更新数据前执行
     *
     * @param userId 操作人 id
     */
    public void preUpdate(String userId) {
        this.updateId = userId;
        this.updateDate = new Date();
    }

}