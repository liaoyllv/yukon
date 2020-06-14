package com.lyl.yukon.upms.api.service;


/**
 * msgLog service
 *
 * @author liaoyl
 */
public interface IMsgService {


    /**
     * 发送邮件消息
     */
    boolean sendSimpleMailMsg(String to, String subject, String content);


}
