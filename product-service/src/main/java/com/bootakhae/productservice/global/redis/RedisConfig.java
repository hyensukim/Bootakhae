package com.bootakhae.productservice.global.redis;

import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

    private final RedisProperties redisProperties;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues() // null 값은 캐싱하지 않음
                .entryTtl(Duration.ofMinutes(5)); // cache ttl
//                .serializeValuesWith(
//                        RedisSerializationContext.SerializationPair
//                                .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
//                );


//        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
//        redisCacheConfigurationMap.put("cacheNames-POST", redisCacheConfiguration);

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
//                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }

    @Bean
    public RedisTemplate<?,?> redisTemplate(){
        RedisTemplate<?,?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress(
                REDISSON_HOST_PREFIX +
                redisProperties.getHost() +
                ":" + redisProperties.getPort()
        );

        return Redisson.create(config);
    }
}
