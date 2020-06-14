package com.lyl.yukon.upms.provider.mq.consumer;

import com.lyl.yukon.common.constant.MsgLogConstant;
import com.lyl.yukon.common.entity.MsgLogDO;
import com.lyl.yukon.upms.provider.dao.MsgLogMapper;
import com.lyl.yukon.common.util.MsgUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/21 2:42 PM
 **/
@Slf4j
@Component
public class Resend {

    @Autowired
    private MsgLogMapper msgLogMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 最大投递次数
    private static final int MAX_TRY_COUNT = 3;

    /**
     * 每30s拉取投递失败的消息, 重新投递
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void resend() {
        log.info("开始执行定时任务(重新投递消息)");

        List<MsgLogDO> msgLogs = msgLogMapper.selectTimeoutMsg();
        msgLogs.forEach(msgLog -> {
            String msgId = msgLog.getMsgId();
            if (msgLog.getTryCount() >= MAX_TRY_COUNT) {
                msgLogMapper.updateStatus(msgId, MsgLogConstant.STATUS_FAIL);
                log.info("超过最大重试次数, 消息投递失败, msgId: {}", msgId);
            } else {
                // 投递次数+1
                msgLogMapper.updateTryCount(msgId, msgLog.getNextTryTime());

                // 重新投递
                CorrelationData correlationData = new CorrelationData(msgId);
                rabbitTemplate.convertAndSend(msgLog.getExchange(), msgLog.getRoutingKey(),
                        MsgUtils.objToMsg(msgLog.getMsg()), correlationData);
                log.info("第 " + (msgLog.getTryCount() + 1) + " 次重新投递消息");
            }
        });

        log.info("定时任务执行结束(重新投递消息)");
    }


}
