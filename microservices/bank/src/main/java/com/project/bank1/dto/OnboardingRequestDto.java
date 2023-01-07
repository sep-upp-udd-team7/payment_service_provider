package com.project.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OnboardingRequestDto {
    private String merchantOrderId;
    private Double amount;
    private LocalDateTime merchantTimestamp;
    private String merchantId;  // TODO SD: merchant id obrisati - kada bude uradjena autentifikacija
    private Boolean qrCode;
}
