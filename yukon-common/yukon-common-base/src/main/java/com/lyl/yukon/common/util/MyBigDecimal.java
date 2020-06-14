package com.lyl.yukon.common.util;


import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * <p>精度计算</p>
 *
 * @author liaoyl
 * @version 1.0 2018/10/11 20:20
 **/
public class MyBigDecimal {

    /**
     * 默认除法运算精度
     */
    private static final int DEFAULT_SCALE = 2;
    /**
     * 默认为四舍五入
     */
    private static final int DEFAULT_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    private static String SEPARATOR = ".";
    private BigDecimal value;

    /**
     * 防止实例化
     */
    private MyBigDecimal() {
    }

    /**
     * 对象转BigDecimal
     */
    public static BigDecimal objectToBigDecimal(@NonNull Object number) {
        BigDecimal value;
        if (number instanceof Integer) {
            value = new BigDecimal(Integer.toString((Integer) number));
        } else if (number instanceof Float) {
            value = new BigDecimal(Float.toString((Float) number));
        } else if (number instanceof Double) {
            value = new BigDecimal(Double.toString((Double) number));
        } else if (number instanceof Short) {
            value = new BigDecimal(Short.toString((Short) number));
        } else if (number instanceof Long) {
            value = new BigDecimal(Long.toString((Long) number));
        } else if (number instanceof String) {
            value = new BigDecimal(number.toString());
        } else { //未知的类型
            throw new IllegalArgumentException("unknown type!");
        }
        return value;
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(Number number) {
        return replace(String.valueOf(number));
    }

    /**
     * 去掉小数点后无效的0
     */
    public static String replace(String number) {
        if (StringUtils.isEmpty(number)) {
            return "0";
        }
        if (number.indexOf(SEPARATOR) > 0) {
            //去掉后面无用的零
            number = number.replaceAll("0+?$", "");
            //如小数点后面全是零则去掉小数点
            number = number.replaceAll("[.]$", "");
        }
        return number;
    }

    public static MyBigDecimal newInstance(@NonNull Object number) {
        MyBigDecimal temp = new MyBigDecimal();
        if (number instanceof BigDecimal) {
            temp.value = (BigDecimal) number;
        } else {
            temp.value = objectToBigDecimal(number);
        }
        return temp;
    }

    /**
     * 减
     */
    public MyBigDecimal subtract(@NonNull Object number) {
        if (number instanceof BigDecimal) {
            value = value.subtract((BigDecimal) number);
        } else {
            value = value.subtract(objectToBigDecimal(number));
        }
        return this;
    }

    /**
     * 乘
     */
    public MyBigDecimal multiply(@NonNull Object number) {
        if (number instanceof BigDecimal) {
            value = value.multiply((BigDecimal) number);
        } else {
            value = value.multiply(objectToBigDecimal(number));
        }
        return this;
    }

    /**
     * 除
     */
    public MyBigDecimal divide(@NonNull Object number) {
        return divide(number, DEFAULT_SCALE, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 除
     *
     * @param scale 精确到小数点后几位数
     */
    public MyBigDecimal divide(@NonNull Object number, int scale) {
        return divide(number, scale, DEFAULT_ROUNDING_MODE);
    }

    /**
     * 除
     *
     * @param scale        精确到小数点后几位数
     * @param roundingMode 精确模式
     * @see BigDecimal
     */
    public MyBigDecimal divide(@NonNull Object number, int scale, int roundingMode) {
        if (number instanceof BigDecimal) {
            value = value.divide((BigDecimal) number, scale, roundingMode);
        } else {
            value = value.divide(objectToBigDecimal(number), scale, roundingMode);
        }
        return this;
    }

    /**
     * 加
     */
    public MyBigDecimal add(@NonNull Object number) {
        if (number instanceof BigDecimal) {
            value = value.add((BigDecimal) number);
        } else {
            value = value.add(objectToBigDecimal(number));
        }
        return this;
    }

    /**
     * 四舍五入
     *
     * @param scale 精确到小数点后几位数
     */
    public MyBigDecimal round(int scale) {
        return round(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入
     *
     * @param scale        精确到小数点后几位数
     * @param roundingMode 精确模式
     * @see BigDecimal
     */
    public MyBigDecimal round(int scale, int roundingMode) {
        if (scale >= 0) {
            value = value.divide(new BigDecimal("1"), scale, roundingMode);
        }
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

}
