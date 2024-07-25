package com.example.coupon_api.controller;

import com.example.coupon_api.controller.dto.CouponIssueResponseDto;
import com.example.coupon_core.exception.CouponIssueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CouponControllerAdvice {
    @ExceptionHandler(CouponIssueException.class)
    public CouponIssueResponseDto couponExceptionHandler(CouponIssueException exception){
        return new CouponIssueResponseDto(false,"발급에 실패하였습니다." );
    }

}
