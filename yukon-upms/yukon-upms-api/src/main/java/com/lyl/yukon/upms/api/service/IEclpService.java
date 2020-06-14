package com.lyl.yukon.upms.api.service;

/**
 * Created by Administrator on 2016/8/26.
 */
public interface IEclpService {
    /**
     * 调用用户中心发送短信
     *
     * @param phone          手机号
     * @param type           短信模版
     * @param terminalType   终端类型
     * @param msgType        1 通知类  2 验证类
     * @param verifySendType 0 文字类  1 语音类
     * @param content        内容
     */
    String sendMessage(String phone, String type, String terminalType, String msgType,
                       String verifySendType, String content) throws Exception;

    /**
     * 用户注册
     *
     * @param userMobile    手机号
     * @param password      密码
     * @param userName      姓名
     * @param userRegSource 注册来源
     * @param userType      用户类型 0-学生 1-老师
     * @param sex           性别
     * @param birthDay      生日
     * @param schoolName    学校简称
     */
    String operateUserSign(String userMobile, String password, String userName,
                           String userRegSource, String userType, String sex, String birthDay,
                           String schoolName) throws Exception;
}
