package com.lyl.yukon.upms.web.config;

import com.lyl.yukon.gateway.interceptor.AuthenticationInterceptor;
import com.lyl.yukon.gateway.interceptor.RequestLimitInterceptor;
import com.lyl.yukon.gateway.interceptor.WriteLockInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;
import java.util.List;

/**
 * <p>拦截器配置</p>
 *
 * @author liaoyl
 * @version 1.0 2019/3/15 17:08
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

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


}
