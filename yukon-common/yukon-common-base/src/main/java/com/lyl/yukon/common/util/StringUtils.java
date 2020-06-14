package com.lyl.yukon.common.util;


import org.apache.commons.lang3.ArrayUtils;

/**
 * 自定义string工具类，继承Apache.StringUtils
 *
 * @author liaoyl
 * @version 2019/4/10
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {


    /**
     * 是否全部都不为空
     *
     * @return true-都不为空
     */
    public static boolean isAllNotBlank(final String... str) {
        if (ArrayUtils.isEmpty(str)) {
            return false;
        }
        for (final String s : str) {
            if (isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否全部都为空
     *
     * @return true-都为空
     */
    public static boolean isAllBlank(final String... str) {
        if (ArrayUtils.isEmpty(str)) {
            return true;
        }
        for (final String s : str) {
            if (isNotBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取扩展名
     *
     * @param fileName 文件名
     */
    public static String getExtName(String fileName) {
        if (isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }



}
