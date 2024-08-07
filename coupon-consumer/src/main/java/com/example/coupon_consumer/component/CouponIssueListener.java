package com.example.coupon_consumer.component;

import com.example.coupon_core.repository.redis.RedisRepository;
import com.example.coupon_core.repository.redis.dto.CouponIssueRequest;
import com.example.coupon_core.service.CouponIssueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.example.coupon_core.util.CouponRedisUtils.getIssueRequestQueueKey;

@RequiredArgsConstructor
@EnableScheduling
@Component
@Slf4j
public class CouponIssueListener {

    private final RedisRepository redisRepository;
    private final CouponIssueService couponIssueService;
    private final String issueRequestQueueKey = getIssueRequestQueueKey();
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000L)
    public void issue() throws JsonProcessingException {
        log.info("listen...");
        while (existCouponIssueTarget()){
            CouponIssueRequest target = getIssueTarget();
            log.info("발급 시작: target : %s".formatted(target));
            couponIssueService.issue(target.couponId(),target.userId());
            log.info("발급 완료: target : %s".formatted(target));
            removeIssuedTarget();
        }
    }

    private boolean existCouponIssueTarget(){
        return redisRepository.lSize(issueRequestQueueKey) > 0;
    }

    private CouponIssueRequest getIssueTarget() throws JsonProcessingException {
        return objectMapper.readValue(redisRepository.lIndex(issueRequestQueueKey,0), CouponIssueRequest.class);
    }

    private void removeIssuedTarget(){
        redisRepository.lPop(issueRequestQueueKey);
    }

}
