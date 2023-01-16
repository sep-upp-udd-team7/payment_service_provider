package com.project.auth_service.dto;

import lombok.Data;

@Data
public class RemovePaymentMethodDto {
    String shopId;

    Long paymentMethodId;
}
