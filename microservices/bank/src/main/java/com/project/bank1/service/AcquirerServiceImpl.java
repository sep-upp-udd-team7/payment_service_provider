package com.project.bank1.service;

import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.mapper.BankMapper;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.AcquirerRepository;
import com.project.bank1.service.interfaces.AcquirerService;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AcquirerServiceImpl implements AcquirerService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private BankService bankService;
    @Autowired
    private AcquirerRepository acquirerRepository;

    public AcquirerDto register(AcquirerDto dto) {
        loggerService.infoLog(String.format("Registering acquirer with merchant ID: {} and merchant password: {}",
                dto.getMerchantId(), dto.getMerchantPassword()));
        Acquirer acquirer = new Acquirer();
        acquirer.setMerchantId(dto.getMerchantId());
        // TODO SD: base64 encode?
        acquirer.setMerchantPassword(dto.getMerchantPassword());
        Bank bank = bankService.findByName(dto.getBank().getName());
        if (bank == null) {
            loggerService.errorLog(String.format("Bank with name {} not found!", dto.getBank().getName()));
            return null;
        }
        acquirer.setBank(bank);
        acquirerRepository.save(acquirer);

        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (a == null) {
            loggerService.errorLog(String.format("Acquirer with merchant ID {} not found!", dto.getMerchantId()));
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
        loggerService.successLog(String.format("Created acquirer with ID: {}", a.getId()));
        return dto;
    }

    public Acquirer findByMerchantId(String merchantId) {
        loggerService.infoLog(String.format("Finding acquirer by merchant ID: {}", merchantId));
        for (Acquirer a: acquirerRepository.findAll()) {
            if (a.getMerchantId().equals(merchantId)) {
                loggerService.successLog(String.format("Found acquirer with ID: {}", a.getId()));
                return a;
            }
        }
        loggerService.errorLog(String.format("Acquirer by merchant ID: {} not found", merchantId));
        return null;
    }
}
