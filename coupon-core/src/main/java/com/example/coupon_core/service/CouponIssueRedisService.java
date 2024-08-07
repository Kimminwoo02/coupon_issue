package com.example.coupon_core.service;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.repository.redis.RedisRepository;
import com.example.coupon_core.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.coupon_core.exception.ErrorCode.DUPLICATED_COUPON_ISSUE;
import static com.example.coupon_core.exception.ErrorCode.INVALID_COUPON_ISSUE_QUANTITY;
import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestKey;

@RequiredArgsConstructor
@Service
public class CouponIssueRedisService {
    private final RedisRepository redisRepository;
    
    public void checkCouponIssueQuantity(CouponRedisEntity couponRedisEntity, long userId){
        if(!availableTotalIssueQuantity(couponRedisEntity.totalQuantity(), couponRedisEntity.id())){
            throw new CouponIssueException(INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량을 초과합니다.");
        }
        if(!availableUserIssueQuantity(couponRedisEntity.id(), userId)){
            throw new CouponIssueException(DUPLICATED_COUPON_ISSUE,"이미 발급한 쿠폰입니다.");
        }    
    }

    public boolean availableTotalIssueQuantity(Integer totalQuantity, long couponId){
        if(totalQuantity == null){
            return true;
        }
        String key = getIssueRequestKey(couponId);
        return totalQuantity > redisRepository.sCard(key);
    }

    public boolean availableUserIssueQuantity(long couponId, long userId){
        String key = getIssueRequestKey(couponId);
        return !redisRepository.sIsMember(key, String.valueOf(userId));
    }
}
