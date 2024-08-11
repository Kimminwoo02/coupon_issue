package com.example.coupon_core.model;

import com.example.coupon_core.exception.CouponIssueException;
import com.example.coupon_core.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType couponType;

    private Integer totalQuantity;

    @Column(nullable = false)
    private int issuedQuantity;

    @Column(nullable = false)
    private int discountAmount;

    @Column(nullable = false)
    private int minAvailableAmount;

    @Column(nullable = false)
    private LocalDateTime dateIssueStart;

    @Column(nullable = false)
    private LocalDateTime dateIssueEnd;

    public boolean availableIssueQuantity(){
        if (totalQuantity == null){
            return true;
        }
        return totalQuantity > issuedQuantity;
    }

    public boolean availableIssueDate(){
        LocalDateTime now = LocalDateTime.now();
        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now);
    }

    public boolean isIssueComplete(){
        LocalDateTime now = LocalDateTime.now();
        return dateIssueEnd.isBefore(now) ||  !availableIssueQuantity();
    }

    public void issue(){
        if(!availableIssueQuantity()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_QUANTITY,"발급 가능한 수량을 초과했습니다. Total: $s, issued: %s".formatted(totalQuantity,issuedQuantity));
        }
        if(!availableIssueDate()){
            throw new CouponIssueException(ErrorCode.INVALID_COUPON_ISSUE_DATE,"발급 가능한 날짜가 아닙니다. issueStart: %s, issueEnd:%s".formatted(dateIssueStart,dateIssueEnd));
        }

        issuedQuantity++;
    }

}
