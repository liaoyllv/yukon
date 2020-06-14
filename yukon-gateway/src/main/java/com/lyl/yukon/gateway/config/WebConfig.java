package com.lyl.yukon.gateway.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lyl.yukon.gateway.interceptor.AuthenticationInterceptor;
import com.lyl.yukon.gateway.interceptor.RequestLimitInterceptor;
import com.lyl.yukon.gateway.interceptor.WriteLockInterceptor;
import com.lyl.yukon.gateway.xss.JsonConverter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>拦截器配置</p>
 *
 * @author liaoyl
 * @version 1.0 2019/3/15 17:08
 **/
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * 白名单
     */
    private final static List<String> WHITE_LIST = Arrays.asList("/upms/system/login",
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/swagger-ui.html/**");

    @Bean
    public RequestLimitInterceptor requestLimitInterceptor() {
        return new RequestLimitInterceptor();
    }

    @Bean
    public WriteLockInterceptor writeLockInterceptor() {
        return new WriteLockInterceptor();
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLimitInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST);
        registry.addInterceptor(writeLockInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST);
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(WHITE_LIST);
        super.addInterceptors(registry);
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        // vue默认只能获取到浏览器提供headers的默认参数
        registry.addMapping("/**").exposedHeaders("Authorization");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 自定义objectMapper
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().createXmlMapper(false).build();
        // 注册xss解析器
        SimpleModule xssModule = new SimpleModule("XssModule");
        // 添加一个自定义Deserializer
        xssModule.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                String value = p.getValueAsString();
                if (StringUtils.isNotBlank(value)) {
                    // 防止 xss
                    value = StringEscapeUtils.escapeHtml4(value);
                    // 去掉头尾空格
                    value = value.trim();
                }
                return value;
            }
        });
        objectMapper.registerModule(xssModule);

        HttpMessageConverter<?> jacksonConverter = converters.get(converters.size() - 1);
        converters.remove(jacksonConverter);
        converters.add(new JsonConverter(objectMapper));
    }

}
