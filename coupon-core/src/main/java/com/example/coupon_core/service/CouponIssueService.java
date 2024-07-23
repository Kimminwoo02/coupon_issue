package com.example.coupon_core.service;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.model.CouponIssue;
import com.example.coupon_core.repository.mysql.CouponIssueJpaRepository;
import com.example.coupon_core.repository.mysql.CouponIssueRepository;
import com.example.coupon_core.repository.mysql.CouponJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponIssueService {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponIssueJpaRepository couponIssueJpaRepository;
    private final CouponIssueRepository couponIssueRepository;

    @Transactional
    public void issue(long couponId, long userId){
        Coupon coupon = findCoupon(couponId);
        coupon.issue();
        saveCouponIssue(couponId,userId);
    }

    /*
    순서
    트랜잭션 시작

    lock획득
        로직 시작
    lock반납


    issue()

    트랜잭션 커밋



     */

    @Transactional(readOnly = true)
    public Coupon findCoupon(long couponId){
        return couponJpaRepository.findById(couponId).orElseThrow(()->{
            throw new CouponIssueException(ErrorCode.COUPON_NOT_EXIST,"쿠폰 정책이 존재하지 않습니다! : %s".formatted(couponId));
        });
    }


    @Transactional
    public Coupon findCouponWithLock(long couponId){
        return couponJpaRepository.findCouponWithLock(couponId).orElseThrow(()->{
            throw new CouponIssueException(ErrorCode.COUPON_NOT_EXIST,"쿠폰 정책이 존재하지 않습니다! : %s".formatted(couponId));
        });
    }

    @Transactional
    public CouponIssue saveCouponIssue(long couponId, long userId){
        checkAlreadyIssued(couponId, userId);
        CouponIssue issue = CouponIssue.builder()
                .couponId(couponId)
                .userId(userId)
                .build();

        return couponIssueJpaRepository.save(issue);
    }

    private void checkAlreadyIssued(long couponId, long userId){
        CouponIssue issue = couponIssueRepository.findFirstCouponIssue(couponId, userId);
        if( issue != null){
            throw new CouponIssueException(ErrorCode.DUPLICATED_COUPON_ISSUE,"이미 발급된 쿠폰입니다. user_id : %s".formatted(couponId));
        }

    }
}
