package com.rand.ishowApi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
    public class RedisConfig {

        @Value("${spring.data.redis.host}")
        private String redisHost;

        @Value("${spring.data.redis.port}")
        private int redisPort;



        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return new LettuceConnectionFactory(redisHost, redisPort);
        }

        @Bean
        public CacheManager cacheManager(RedisConnectionFactory factory) {
            return RedisCacheManager.builder(factory).build();
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(
                RedisConnectionFactory connectionFactory) {

            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(connectionFactory);

            template.setKeySerializer(RedisSerializer.string());
            template.setValueSerializer(RedisSerializer.json());

            template.setHashKeySerializer(RedisSerializer.string());
            template.setHashValueSerializer(RedisSerializer.json());

            template.afterPropertiesSet();
            return template;
        }

    }
