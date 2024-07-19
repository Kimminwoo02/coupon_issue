package com.example.coupon_core.repository.mysql;

import com.example.coupon_core.model.CouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import  static com.example.coupon_core.model.QCouponIssue.couponIssue;

@RequiredArgsConstructor
@Repository
public class CouponIssueRepository {
    private final JPQLQueryFactory queryFactory;

    public CouponIssue findFirstCouponIssue(long couponId,long userId){
        return queryFactory.selectFrom(couponIssue)
                .where(couponIssue.couponId.eq(couponId))
                .where(couponIssue.userId.eq(userId))
                .fetchFirst();
    }



}
