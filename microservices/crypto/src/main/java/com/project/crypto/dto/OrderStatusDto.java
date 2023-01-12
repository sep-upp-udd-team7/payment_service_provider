package com.project.crypto.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusDto {
    private String status;
    private String orderId;
    private double amount;

    public OrderStatusDto(String status, String orderId, double amount) {
        this.status = status;
        this.orderId = orderId;
        this.amount = amount;
    }
}
