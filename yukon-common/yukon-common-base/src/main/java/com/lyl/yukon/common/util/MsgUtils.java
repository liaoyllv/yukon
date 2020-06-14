package com.lyl.yukon.common.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;

/**
 * <p></p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/21 1:28 PM
 **/
public class MsgUtils {

    public static Message objToMsg(Object obj) {
        if (null == obj) {
            return null;
        }
        Message message = MessageBuilder.withBody(JsonUtils.toJson(obj).getBytes()).build();
        // 消息持久化
        message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
        return message;
    }

    public static <T> T msgToObj(Message message, Class<T> clazz) {
        if (null == message || null == clazz) {
            return null;
        }
        String str = new String(message.getBody());
        return JsonUtils.toObject(str, clazz);
    }
}
