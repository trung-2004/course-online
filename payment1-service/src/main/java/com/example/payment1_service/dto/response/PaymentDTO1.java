package com.example.payment1_service.dto.response;

import lombok.Builder;

public abstract class PaymentDTO1 {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;

        // Public constructor
        public VNPayResponse(String code, String message, String paymentUrl) {
            this.code = code;
            this.message = message;
            this.paymentUrl = paymentUrl;
        }
    }
}
