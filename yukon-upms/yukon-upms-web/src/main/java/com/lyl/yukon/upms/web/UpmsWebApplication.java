package com.lyl.yukon.upms.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;


/**
 * Spring boot 启动入口
 *
 * @author ligj
 */
@SpringBootApplication(scanBasePackages = "com.lyl")
@EnableSwagger2
@ComponentScan(basePackages = {"com.lyl.yukon"})
@ImportResource("classpath:dubbo-consumer.xml")
public class UpmsWebApplication {

    private static final Logger logger = LoggerFactory.getLogger(UpmsWebApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(UpmsWebApplication.class, args);
        logger.info("\n\n========= ============= =========\n========= UpmsWebApplication start success =========\n========= ============= =========\n\n");
    }
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("Authorization");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     */
    @Bean
    public FilterRegistrationBean<Filter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        CorsFilter corsFilter = new CorsFilter(source);
        // 优先级最高
        FilterRegistrationBean bean = new FilterRegistrationBean(corsFilter);
        bean.setOrder(Integer.MIN_VALUE);
        return bean;
    }


//    @Bean
//    @Primary
//    public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
//        //解析器
//        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        //注册xss解析器
//        SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
//        xssModule.addSerializer(new XssStringJsonSerializer());
//        objectMapper.registerModule(xssModule);
//        //返回
//        return objectMapper;
//    }


}
