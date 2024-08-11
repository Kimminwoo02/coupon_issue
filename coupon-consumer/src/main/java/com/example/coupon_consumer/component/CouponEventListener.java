package com.example.coupon_consumer.component;

import com.example.coupon_core.model.event.CouponIssueCompleteEvent;
import com.example.coupon_core.service.CouponCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponEventListener {

    private final CouponCacheService couponCacheService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void issueComplete(CouponIssueCompleteEvent event){
        log.info("issue Complete. cache refresh Start couponId: %s".formatted(event.couponId()));

        couponCacheService.putCouponCache(event.couponId());
        couponCacheService.putCouponLocalCache(event.couponId());

        log.info("issue Complete. cache refresh end couponId: %s".formatted(event.couponId()));

    }
}
