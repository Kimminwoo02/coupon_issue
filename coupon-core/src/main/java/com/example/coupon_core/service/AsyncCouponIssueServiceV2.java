package com.example.coupon_core.service;

import com.example.coupon_core.component.DistributeLockExecutor;
import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.repository.redis.RedisRepository;
import com.example.coupon_core.repository.redis.dto.CouponIssueRequest;
import com.example.coupon_core.repository.redis.dto.CouponRedisEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.coupon_core.exception.ErrorCode.FAIL_COUPON_ISSUE_REQUEST;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestQueueKey;

@Service
@RequiredArgsConstructor
public class AsyncCouponIssueServiceV2 {

    private final RedisRepository redisRepository;
    private final CouponCacheService couponCacheService;



    public void issue(long couponId, long userId){

        CouponRedisEntity coupon = couponCacheService.getCouponLocalCache(couponId);
        coupon.checkIssuableCoupon();
        issueRequest(couponId,userId, coupon.totalQuantity());
    }

    private void issueRequest(long couponId, long userId, Integer totalIssueQuantity){
        if(totalIssueQuantity == null){
            redisRepository.issueRequest(couponId, userId, Integer.MAX_VALUE);
        }
        redisRepository.issueRequest(couponId, userId, totalIssueQuantity);

    }

}
