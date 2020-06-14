package com.lyl.yukon.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.MissingResourceException;

/**
 * <p>配置文件常量</p>
 *
 * @author liaoyl
 * @version 1.0 2019/07/25 11:35
 **/
@Component
public class PropertyUtil {

    private static Environment env;

    /**
     * Get a value based on key , if key does not exist , null is returned
     */
    public static String getProperty(String key) {
        try {
            return env.getProperty(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    /**
     * Get a value based on key , if key does not exist , null is returned
     */
    public static String getProperty(String key, String defaultValue) {
        try {
            String value = env.getProperty(key);
            if (ObjectUtils.isEmpty(value)) {
                return defaultValue;
            }
            return value;
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

    @Autowired
    protected void set(Environment env) {
        PropertyUtil.env = env;
    }

}