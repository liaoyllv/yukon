package com.lyl.yukon.upms.provider.service;

import com.lyl.yukon.upms.UpmsProviderApplication;
import com.lyl.yukon.upms.provider.mq.consumer.MailConsumer;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DisplayName("邮件测试")
@SpringBootTest(classes = UpmsProviderApplication.class)
public class MailConsumerTest {

    @Autowired
    private MailConsumer mailService;


    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("liaoyanglong@icloud.com", "测试0","加油！");
    }

    @Test
    public void sendHtmlMail() {
    }

    @Test
    public void sendAttachmentsMail() {
    }

    @Test
    public void sendInlineResourceMail() {
    }
}