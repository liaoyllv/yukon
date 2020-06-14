package com.lyl.yukon.gateway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口访问权限
 * 分为四种权限：browse、create、update、delete
 * 格式为“资源:权限类型”，如：user:browse
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

    /**
     * 权限值
     */
    String value();
}
