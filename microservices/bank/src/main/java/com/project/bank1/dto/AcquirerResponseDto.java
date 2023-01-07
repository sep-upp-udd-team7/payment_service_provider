package com.project.bank1.dto;

import lombok.*;
import reactor.core.publisher.Mono;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcquirerResponseDto {
    private String paymentUrl;
    private String paymentId;
    private String acquirer;
    private String acquirerBankAccount;
    private Double amount;
}
