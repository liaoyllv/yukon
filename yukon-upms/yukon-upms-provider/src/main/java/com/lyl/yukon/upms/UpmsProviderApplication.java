package com.lyl.yukon.upms;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;


/**
 * 启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.lyl"}) // 扫描所有
@MapperScan("com.lyl.yukon.upms.provider.dao") // dao 扫描的目录
@ImportResource("classpath:dubbo-provider.xml")
public class UpmsProviderApplication {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(UpmsProviderApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(UpmsProviderApplication.class)
                // 非 Web 应用
                .web(WebApplicationType.NONE)
                .run(args);
        logger.info("\n\r====== UpmsProviderApplication start ======\n\r");
    }
}
