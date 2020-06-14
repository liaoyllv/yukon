package com.lyl.yukon.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis配置
 *
 * @author ligj
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisRepository redisRepository(RedisTemplate<String, String> redisTemplate) {
        return new RedisRepository(redisTemplate);
    }

    @Bean
    public RedissonClient redissonClient(RedisProperties redisProperties) {

        Config config = new Config();
        //单机连接模式
        SingleServerConfig singleServerConfig = config.useSingleServer();
        String schema = redisProperties.isSsl() ? "rediss://" : "redis://";
        singleServerConfig.setAddress(schema + redisProperties.getHost() + ":" + redisProperties.getPort());
        singleServerConfig.setDatabase(redisProperties.getDatabase());
        //判断密码设置
        if (redisProperties.getPassword() != null) {
            singleServerConfig.setPassword(redisProperties.getPassword());
        }
        return Redisson.create(config);
    }
}