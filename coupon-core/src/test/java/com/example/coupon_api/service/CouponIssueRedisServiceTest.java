package com.example.coupon_api.service;

import com.example.coupon_api.TestConfig;
import com.example.coupon_core.service.CouponIssueRedisService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;

public class CouponIssueRedisServiceTest extends TestConfig {

    @Autowired
    CouponIssueRedisService sut;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void clear(){
        Collection<String> redisKeys = redisTemplate.keys("*");
        redisTemplate.delete(redisKeys);
    }


    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 존재하면 true를 반환")
    void test(){
        // given
        int totalIssueQuantity = 10;
        long userId = 1;
        //when
        boolean result = sut.availableTotalIssueQuantity(totalIssueQuantity,userId);

        Assertions.assertTrue(result);
    }



}

