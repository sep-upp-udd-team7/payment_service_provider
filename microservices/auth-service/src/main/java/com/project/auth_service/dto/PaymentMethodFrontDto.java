package com.project.auth_service.dto;

import lombok.Data;

@Data
public class PaymentMethodFrontDto {

    private Long id;
    private String name;
    private boolean canSubscribe;
}
