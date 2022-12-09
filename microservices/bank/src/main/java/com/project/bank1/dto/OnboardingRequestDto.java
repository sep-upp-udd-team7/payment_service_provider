package com.project.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnboardingRequestDto {
    private String merchantOrderId;
    private Double amount;
    private String merchantId;

    // TODO SD: dodati jos podataka
}
