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
import java.util.stream.IntStream;

import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;

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

    @Test
    @DisplayName("쿠폰 수량 검증 - 발급 가능 수량이 소진되면 존재하면 false 반환")
    void test2(){
        // given
        int totalIssueQuantity = 10;
        long couponId = 1;
        IntStream.range(0,totalIssueQuantity).forEach(userId->{
            redisTemplate.opsForSet().add(getIssueRequestKey(couponId), String.valueOf(userId));
        });
        //when
        boolean result = sut.availableTotalIssueQuantity(totalIssueQuantity,couponId);

        Assertions.assertFalse(result);
    }


    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재하지 않으면 true를 반환")
    void test3(){
        // given
        long couponId = 1;
        long userId = 1;

        //when
        boolean result = sut.availableUserIssueQuantity(couponId,userId);

        //then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("쿠폰 중복 발급 검증 - 발급된 내역에 유저가 존재하면 false 반환")
    void test4(){
        // given
        long couponId = 1;
        long userId = 1;
        redisTemplate.opsForSet().add(getIssueRequestKey(couponId), String.valueOf(userId));
        //when
        boolean result = sut.availableUserIssueQuantity(couponId,userId);

        //then
        Assertions.assertFalse(result);
    }



}

