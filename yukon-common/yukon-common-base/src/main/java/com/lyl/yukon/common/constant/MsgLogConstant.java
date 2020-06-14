package com.lyl.yukon.common.constant;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/21 11:34 AM
 **/
public class MsgLogConstant {

    private MsgLogConstant() {
    }

    /**
     * 状态: 0投递中 1投递成功 2投递失败 3已消费
     */
    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 2;
    public static final int STATUS_CONSUMED = 3;

}
