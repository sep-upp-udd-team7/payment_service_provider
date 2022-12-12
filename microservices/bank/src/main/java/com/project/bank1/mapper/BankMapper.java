package com.project.bank1.mapper;

import com.project.bank1.dto.BankDto;
import com.project.bank1.model.Bank;

public class BankMapper {

    public BankDto mapModelToDto(Bank bank) {
        BankDto dto = new BankDto();
        dto.setId(bank.getId());
        dto.setName(bank.getName());
        dto.setBankUrl(bank.getBankUrl());
        return dto;
    }

}
