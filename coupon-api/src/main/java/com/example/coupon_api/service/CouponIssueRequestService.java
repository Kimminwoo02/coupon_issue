package com.example.coupon_api.service;

import com.example.coupon_api.controller.dto.CouponIssueRequestDto;
import com.example.coupon_core.component.DistributeLockExecutor;
import com.example.coupon_core.service.CouponIssueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponIssueRequestService {
    private final CouponIssueService couponIssueService;
    private final DistributeLockExecutor distributeLockExecutor;

    public void issueRequestV1(CouponIssueRequestDto requestDto){
//        distributeLockExecutor.execute ("lock_name "+ requestDto.couponId(), 10000,10000, ()->{ 레디스 락 제거
            couponIssueService.issue(requestDto.couponId(), requestDto.userId());
//        });
        log.info("쿠폰 발급 완료. couponId: %s, userId: %s".formatted(requestDto.couponId(), requestDto.userId()));
    }

    // 1.mysql, 2.redis
}
