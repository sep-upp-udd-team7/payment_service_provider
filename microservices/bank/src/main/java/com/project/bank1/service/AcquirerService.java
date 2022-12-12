package com.project.bank1.service;

import com.netflix.discovery.converters.Auto;
import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.dto.AcquirerResponseDto;
import com.project.bank1.dto.BankDto;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.AcquirerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirerService {
    @Autowired
    private BankService bankService;
    @Autowired
    private AcquirerRepository acquirerRepository;

    public AcquirerDto register(AcquirerDto dto) {
        Acquirer acquirer = new Acquirer();
        acquirer.setMerchantId(dto.getMerchantId());
        acquirer.setMerchantPassword(dto.getMerchantPassword());
        Bank bank = bankService.findByName(dto.getBank().getName());
        if (bank == null) {
            return null;
        }
        acquirer.setBank(bank);
        acquirerRepository.save(acquirer);

        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (a == null) {
            return null;
        }
        dto.setId(a.getId());
        return dto;
    }

    public Acquirer findByMerchantId(String merchantId) {
        for (Acquirer a: acquirerRepository.findAll()) {
            if (a.getMerchantId().equals(merchantId)) {
                return a;
            }
        }
        return null;
    }
}
