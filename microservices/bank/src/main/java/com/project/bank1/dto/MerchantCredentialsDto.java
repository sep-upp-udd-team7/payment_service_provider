package com.project.bank1.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MerchantCredentialsDto {
    private String merchantId;
    private String merchantPassword;
    private String bankName;
}
