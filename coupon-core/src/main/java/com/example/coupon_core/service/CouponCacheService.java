package com.example.coupon_core.service;

import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.repository.redis.dto.CouponRedisEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponCacheService {
    private final CouponIssueService couponIssueService;


    @Cacheable(cacheNames = "coupon")
    public CouponRedisEntity getCouponCache(long couponId){
        Coupon coupon = couponIssueService.findCoupon(couponId);
        return new CouponRedisEntity(coupon);
    }
}
