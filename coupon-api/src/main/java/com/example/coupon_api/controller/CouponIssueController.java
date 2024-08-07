package com.example.coupon_api.controller;

import com.example.coupon_api.controller.dto.CouponIssueRequestDto;
import com.example.coupon_api.controller.dto.CouponIssueResponseDto;
import com.example.coupon_api.service.CouponIssueRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponIssueController {
    private final CouponIssueRequestService couponIssueRequestService;

    @PostMapping("/v1/issue")
    public CouponIssueResponseDto issueV1(@RequestBody CouponIssueRequestDto body){
        couponIssueRequestService.issueRequestV1(body);
        return new CouponIssueResponseDto(true,null);
    }


    @PostMapping("/v1/issue-async")
    public CouponIssueResponseDto asyncIssueV1(@RequestBody CouponIssueRequestDto body){
        couponIssueRequestService.asyncIssueRequestV1(body);
        return new CouponIssueResponseDto(true,null);
    }

    @PostMapping("/v2/issue-async")
    public CouponIssueResponseDto asyncIssueV2(@RequestBody CouponIssueRequestDto body){
        couponIssueRequestService.asyncIssueRequestV2(body);
        return new CouponIssueResponseDto(true,null);
    }

}
