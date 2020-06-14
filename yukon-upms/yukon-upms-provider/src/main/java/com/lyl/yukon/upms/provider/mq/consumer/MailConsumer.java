package com.lyl.yukon.upms.provider.mq.consumer;

import com.lyl.yukon.common.constant.MqConstant;
import com.lyl.yukon.common.constant.MsgLogConstant;
import com.lyl.yukon.common.entity.MsgLogDO;
import com.lyl.yukon.upms.provider.dao.MsgLogMapper;
import com.lyl.yukon.common.util.MsgUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * <p>邮件服务</p>
 *
 * @author liaoyl
 * @version 1.0 2020/04/20 4:05 PM
 **/
@Component
@Slf4j
public class MailConsumer {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MsgLogMapper msgLogMapper;

    /**
     * 发送普通邮件
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 内容
     */
    public boolean sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            mailSender.send(message);
            log.info("简单邮件已经发送。");
        } catch (Exception e) {
            log.error("发送简单邮件时发生异常！", e);
            return false;
        }
        return true;
    }

    public void sendHtmlMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            // true表示需要创建一个 multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("html邮件发送成功");
        } catch (MessagingException e) {
            log.error("发送html邮件时发生异常！", e.getMessage());
        }

    }

    public void sendAttachmentsMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            File file = new File(filePath);
            // 获取文件名
            String fileName = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/") + 1);
            helper.addAttachment(fileName, file);
            mailSender.send(message);
            log.info("带附件的邮件已经发送。");
        } catch (MessagingException e) {
            log.error("发送带附件的邮件时发生异常！", e);
        }
    }

    public void sendInlineResourceMail(String to, String subject, String content, String rscPath, String rscId) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            FileSystemResource res = new FileSystemResource(new File(rscPath));
            helper.addInline(rscId, res);

            mailSender.send(message);
            log.info("嵌入静态资源的邮件已经发送。");
        } catch (javax.mail.MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }

    @RabbitListener(queues = MqConstant.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        log.info("收到消息: {}", message.toString());

        Map map = MsgUtils.msgToObj(message, Map.class);
        String msgId = map.get("msgId").toString();

        MsgLogDO msgLog = msgLogMapper.selectByPrimaryKey(msgId);
        if (null == msgLog || msgLog.getStatus().equals(MsgLogConstant.STATUS_CONSUMED)) {
            // 消费幂等性
            log.info("重复消费, msgId: {}", msgId);
            return;
        }

        MessageProperties properties = message.getMessageProperties();
        if (sendSimpleMail(map.get("to").toString(), map.get("subject").toString(), map.get("content").toString())) {
            msgLogMapper.updateStatus(msgId, MsgLogConstant.STATUS_SUCCESS);
            // 消费确认
            channel.basicAck(properties.getDeliveryTag(), false);
        } else {
            channel.basicNack(properties.getDeliveryTag(), false, true);
        }
    }
}


