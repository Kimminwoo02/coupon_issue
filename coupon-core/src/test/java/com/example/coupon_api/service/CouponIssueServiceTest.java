package com.example.coupon_core.service;

import com.example.coupon_core.TestConfig;
import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import com.example.coupon_core.model.Coupon;
import com.example.coupon_core.model.CouponIssue;
import com.example.coupon_core.model.CouponType;
import com.example.coupon_core.repository.mysql.CouponIssueJpaRepository;
import com.example.coupon_core.repository.mysql.CouponIssueRepository;
import com.example.coupon_core.repository.mysql.CouponJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponIssueServiceTest extends TestConfig {

    @Autowired
    CouponIssueService sut;
    @Autowired
    CouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired
    CouponIssueRepository couponIssueRepository;
    @Autowired
    CouponJpaRepository couponJpaRepository;

    @BeforeEach
    void clean(){
        couponJpaRepository.deleteAllInBatch();
        couponIssueJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("중복내역이 있으면 발급이 실패하는지")
    void test1(){
        // given
        CouponIssue couponIssue = CouponIssue.builder()
                .couponId(1L)
                .userId(1L)
                .build();
        couponIssueJpaRepository.save(couponIssue);

        //when
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class,()->{
            sut.saveCouponIssue(couponIssue.getCouponId(), couponIssue.getUserId());
        });

        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_COUPON_ISSUE);

        //then
    }


    @Test
    @DisplayName("쿠폰 발급내역이 존재하지 않는다면 쿠폰을 발급한다.")
    void test2(){
        // given
        long couponId = 1L;
        long userId = 1L;

        //when
        CouponIssue result = sut.saveCouponIssue(couponId, userId);

        //then
        Assertions.assertTrue(couponIssueJpaRepository.findById(result.getId()).isPresent());
    }

    @Test
    @DisplayName("발급 수량, 기한, 중복 발급문제가 없다면 쿠폰을 발급한다.")
    void issueTest(){
        //given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 쿠폰 테스트")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        couponJpaRepository.save(coupon);
        //when
        sut.issue(coupon.getId(), userId);

        //then
        Coupon couponResult = couponJpaRepository.findById(coupon.getId()).get();
        Assertions.assertEquals(couponResult.getIssuedQuantity(),1);

        CouponIssue couponIssueResult = couponIssueRepository.findFirstCouponIssue(coupon.getId(),userId);
        Assertions.assertNotNull(couponIssueResult);
    }


    @Test
    @DisplayName("발급 수량에 문제가 있다면 예외를 반환한다.")
    void issueTest2(){
        //given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 쿠폰 테스트")
                .totalQuantity(100)
                .issuedQuantity(100)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(1))
                .build();

        couponJpaRepository.save(coupon);
        //when
        sut.issue(coupon.getId(), userId);

        //then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, ()->{
            sut.issue(coupon.getId(), userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_QUANTITY);
    }


    @Test
    @DisplayName("발급 기한에 문제가 있다면 예외를 반환한다.")
    void issueTest3(){
        //given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 쿠폰 테스트")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();

        couponJpaRepository.save(coupon);
        //when
        sut.issue(coupon.getId(), userId);

        //then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, ()->{
            sut.issue(coupon.getId(), userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.INVALID_COUPON_ISSUE_DATE);
    }


    @Test
    @DisplayName("중복발급에 문제가 있다면 예외를 반환한다.")
    void issueTest4(){
        //given
        long userId = 1;
        Coupon coupon = Coupon.builder()
                .couponType(CouponType.FIRST_COME_FIRST_SERVED)
                .title("선착순 쿠폰 테스트")
                .totalQuantity(100)
                .issuedQuantity(0)
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        couponJpaRepository.save(coupon);

        CouponIssue couponIssue = CouponIssue.builder()
            .couponId(coupon.getId())
            .userId(userId)
            .build();
        couponIssueJpaRepository.save(couponIssue);

        //when
        sut.issue(coupon.getId(), userId);

        //then
        CouponIssueException exception = Assertions.assertThrows(CouponIssueException.class, ()->{
            sut.issue(coupon.getId(), userId);
        });
        Assertions.assertEquals(exception.getErrorCode(), ErrorCode.DUPLICATED_COUPON_ISSUE);
    }


}