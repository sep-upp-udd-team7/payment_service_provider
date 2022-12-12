package com.project.bank1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcquirerDto {
    private Long id;
    private String merchantId;
    private String merchantPassword;
    private BankDto bank;
}
