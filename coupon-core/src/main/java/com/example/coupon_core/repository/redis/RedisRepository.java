package com.example.coupon_core.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Boolean zAdd(String key, String value, double score){
        return redisTemplate.opsForZSet().add(key, value, score);
    }
}
