package com.example.coupon_core.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    @DisplayName("발급 수량이 남아있다면 true를 반환한다.")
    void availableIssueQuantity() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(99)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("발급 수량이 남아있지 않다면 false를 반환")
    void availableIssueQuantity2() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(100)
                .issuedQuantity(100)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("최대 발급수량이 설정되지 않았다면 true를 반환한다.")
    void availableIssueQuantity3() {
        // given
        Coupon coupon = Coupon.builder()
                .totalQuantity(null)
                .issuedQuantity(100)
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("발급기간이 시작되지 않았다면 false를 반환한다.")
    void availableIssueDate_1() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().plusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertFalse(result);
    }


    @Test
    @DisplayName("발급기간에 해당되면 true를 반환")
    void availableIssueDate_2() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(1))
                .dateIssueEnd(LocalDateTime.now().plusDays(2))
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertTrue(result);
    }


    @Test
    @DisplayName("발급기간이 종료되면 false를 반환")
    void availableIssueDate_3() {
        // given
        Coupon coupon = Coupon.builder()
                .dateIssueStart(LocalDateTime.now().minusDays(2))
                .dateIssueEnd(LocalDateTime.now().minusDays(1))
                .build();

        // when
        boolean result = coupon.availableIssueQuantity();


        // then
        Assertions.assertFalse(result);
    }


    @Test
    void availableIssueDate() {
    }
}