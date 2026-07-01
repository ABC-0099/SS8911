package com.meta12.SS8911.dto;

import com.meta12.SS8911.config.OrderPayStatus;
import com.meta12.SS8911.entity.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderPayDTO {

    private Long id; // 기본키
    private Category category; // 카테고리
    private String price; // 결제금액
    private LocalDateTime payday; // 결제날짜
    private String payType; // 결제수단 (ex 카드)
    private Long siteUserId; // 사용자 ID
    private String cardNumber; // 카드번호
    private String instructorName; // 강사이름

    // --- [토스 페이먼츠 연동을 위해 추가된 필드] ---

    private String orderId; // 주문 고유번호
    private String paymentKey; // 토스 결제 승인 키
    private OrderPayStatus status; // 결제 상태 (SUCCESS, CANCEL, FAILED)
}
