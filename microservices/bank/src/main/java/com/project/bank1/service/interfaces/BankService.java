package com.project.bank1.service.interfaces;

import com.project.bank1.dto.BankDto;
import com.project.bank1.model.Bank;

import java.util.List;

public interface BankService {
    Bank findByName(String name);

    List<BankDto> getAll();
}
