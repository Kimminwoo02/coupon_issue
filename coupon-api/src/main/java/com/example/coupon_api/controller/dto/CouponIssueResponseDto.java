package com.example.coupon_api.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonInclude(value = NON_NULL)
public record CouponIssueResponseDto(boolean isSuccess, String comment){
}
