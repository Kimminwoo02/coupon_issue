package com.example.coupon_core.service;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.repository.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;

@Service
@RequiredArgsConstructor
public class AsyncCouponIssueServiceV1 {
    private final RedisRepository redisRepository;
    private final CouponIssueRedisService couponIssueRedisService;
    private final CouponIssueService couponIssueService;
    public void issue(long couponId, long userId){
        // 1. 유저의 요청을 sorted set에 적체
//        String key = "issue.request.sortedset.couponId=%s".formatted(couponId);
//        redisRepository.zAdd(key,String.valueOf(userId),System.currentTimeMillis());

        Coupon coupon = couponIssueService.findCoupon(couponId);
        if(!coupon.availableIssueDate()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE,"발급 기간이 지났습니다");
        }
        if(couponIssueRedisService.availableTotalIssueQuantity(coupon.getTotalQuantity(),couponId)){

            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량을 초과합니다.");
        }

        if(couponIssueRedisService.availableUserIssueQuantity(couponId,userId)){
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE,"이미 발급한 쿠폰입니다.");
        }
        issueRequest(couponId,userId);

    }

    private void issueRequest(long couponId, long userId){
        redisRepository.sAdd(getIssueRequestKey(couponId), String.valueOf(userId));
        // 쿠폰 발급 큐 적재
    }

}
