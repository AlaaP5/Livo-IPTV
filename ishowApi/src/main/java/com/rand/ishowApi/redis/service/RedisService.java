package com.rand.ishowApi.redis.service;


import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // Set key with Duration TTL
    public void setKey(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    // Expire key with Duration
    public boolean expireKey(String key, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, ttl));
    }

    // Get TTL in seconds
    public long getTTL(String key) {
        return redisTemplate.getExpire(key);
    }

    // Get key
    public Object getKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Delete key
    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public Set<String> getAllKeys(String pattern) {
        return redisTemplate.keys(pattern + "*");
    }

    private String generateKeyName(String keyName, Object... params) {
        StringBuilder sb = new StringBuilder(keyName);
        for (Object param : params) {
            sb.append(":").append(param);
        }
        return sb.toString();
    }

    public void setKey(String key, Object value, Duration ttl,Object... params) {
        String keyName=generateKeyName(key,params);
        redisTemplate.opsForValue().set(keyName, value, ttl);
    }

    public <T> T getKey(String key,Class<T> type,Object... params) {
        String keyName=generateKeyName(key,params);
        Object value= redisTemplate.opsForValue().get(keyName);
        return value == null ? null : (T) value;

    }
    public <T> T getKey(String key, ParameterizedTypeReference<T> type, Object... params) {
        String keyName = generateKeyName(key, params);
        Object value = redisTemplate.opsForValue().get(keyName);
        return value == null ? null : (T) value;
    }

}
