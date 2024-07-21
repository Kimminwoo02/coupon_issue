package com.example.coupon_core.repository.mysql;

import com.example.coupon_core.model.CouponIssue;
import com.example.coupon_core.model.QCouponIssue;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponIssueRepository {
    private final JPQLQueryFactory queryFactory;

    public CouponIssue findFirstCouponIssue(long couponId, long userId){
        return queryFactory.selectFrom(QCouponIssue.couponIssue)
                .where(QCouponIssue.couponIssue.couponId.eq(couponId))
                .where(QCouponIssue.couponIssue.userId.eq(userId))
                .fetchFirst();
    }



}
