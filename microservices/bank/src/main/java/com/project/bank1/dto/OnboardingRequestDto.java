package com.project.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OnboardingRequestDto {
    private String merchantOrderId;
    private Double amount;
    private String merchantId;
    private LocalDateTime merchantTimestamp;

    // TODO SD: dodati jos podataka
}
