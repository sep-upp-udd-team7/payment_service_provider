package com.project.paypal.dto;

import lombok.Data;

@Data
public class CreatePaymentDto {

    private String amount;

    private String transactionId;

    private String shopId;
}
