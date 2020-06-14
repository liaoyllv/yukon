package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.common.constant.MqConstant;
import com.lyl.yukon.common.constant.MsgLogConstant;
import com.lyl.yukon.common.entity.MsgLogDO;
import com.lyl.yukon.upms.api.service.IMsgService;
import com.lyl.yukon.upms.provider.dao.MsgLogMapper;
import com.lyl.yukon.common.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/21 11:26 AM
 **/
@Slf4j
@Service("msgService")
@Transactional(readOnly = true, rollbackFor = RuntimeException.class)
public class MsgServiceImpl implements IMsgService {

    @Autowired
    private MsgLogMapper msgLogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean sendSimpleMailMsg(String to, String subject, String content) {

        MsgLogDO msgLog = new MsgLogDO();
        msgLog.setMsgId(UUID.randomUUID().toString());
        msgLog.setExchange(MqConstant.MAIL_EXCHANGE_NAME);
        msgLog.setRoutingKey(MqConstant.MAIL_ROUTING_KEY_NAME);
        msgLog.setStatus(MsgLogConstant.STATUS_SENDING);
        msgLogMapper.insertSelective(msgLog);

        // 发送消息
        CorrelationData correlationData = new CorrelationData(msgLog.getMsgId());
        HashMap<String, Object> mail = new HashMap<>();
        mail.put("to", to);
        mail.put("subject", subject);
        mail.put("content", content);
        mail.put("msgId", msgLog.getMsgId());
        rabbitTemplate.convertAndSend(MqConstant.MAIL_EXCHANGE_NAME, MqConstant.MAIL_ROUTING_KEY_NAME, MsgUtils.objToMsg(mail), correlationData);
        return true;
    }


}
