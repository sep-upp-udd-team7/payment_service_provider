package com.project.paypal.dto;

import lombok.Data;

@Data
public class ExecutePaymentDto {
    private String paymentId;
    private String payerId;
    private String transactionId;
}
