package com.example.coupon_core.service;

import com.example.coupon_core.component.DistributeLockExecutor;
import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.repository.redis.RedisRepository;
import com.example.coupon_core.repository.redis.dto.CouponIssueRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.coupon_core.exception.ErrorCode.*;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestQueueKey;

@Service
@RequiredArgsConstructor
public class AsyncCouponIssueServiceV1 {
    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public void issue(long couponId, long userId){
        // 1. 유저의 요청을 sorted set에 적체
//        String key = "issue.request.sortedset.couponId=%s".formatted(couponId);
//        redisRepository.zAdd(key,String.valueOf(userId),System.currentTimeMillis());

        Coupon coupon = couponIssueService.findCoupon(couponId);
        if(!coupon.availableIssueDate()){
            throw new CouponIssueException(INVALID_COUPON_ISSUE_DATE,"발급 기간이 지났습니다");
        }
        distributeLockExecutor.execute("lock_%s".formatted(couponId),3000,3000,()->{
            if(couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(),couponId)){
                throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량을 초과합니다.");
            }
            if(couponIssueRedisService.availableUserIssueQuantity(couponId,userId)){
                throw new CouponIssueException(DUPLICATED_COUPON_ISSUE,"이미 발급한 쿠폰입니다.");
            }
            
            issueRequest(couponId,userId);
        });


    }

    private void issueRequest(long couponId, long userId){
        CouponIssueRequest issueRequest = new CouponIssueRequest(couponId, userId);
        try{
            String value = objectMapper.writeValueAsString(issueRequest);
            redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));
            redisRepository.rPush(getIssueRequestQueueKey(),value);
        } catch (JsonProcessingException e) {
            throw new CouponIssueException(FAIL_COUPON_ISSUE_REQUEST,"input : %s".formatted(issueRequest));
        }

        // 쿠폰 발급 큐 적재
    }

}
