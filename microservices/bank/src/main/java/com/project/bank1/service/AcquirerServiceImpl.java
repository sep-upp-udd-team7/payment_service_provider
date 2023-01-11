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

import java.text.MessageFormat;

@Service
public class AcquirerServiceImpl implements AcquirerService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private BankService bankService;
    @Autowired
    private AcquirerRepository acquirerRepository;

    public AcquirerDto register(AcquirerDto dto) {
        loggerService.infoLog(MessageFormat.format("Registering acquirer with merchant ID: {0} and merchant password: {1}",
                dto.getMerchantId(), dto.getMerchantPassword()));
        Acquirer acquirer = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if(acquirer == null){
            acquirer = new Acquirer();
            acquirer.setMerchantId(dto.getMerchantId());
            // TODO SD: base64 encode?
            acquirer.setMerchantPassword(dto.getMerchantPassword());
            Bank bank = bankService.findByName(dto.getBank().getName());
            if (bank == null) {
                loggerService.errorLog(MessageFormat.format("Bank with name {0} not found!", dto.getBank().getName()));
                return null;
            }
            acquirer.setBank(bank);
            acquirerRepository.save(acquirer);
        }

        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (a == null) {
            loggerService.errorLog(MessageFormat.format("Acquirer with merchant ID {0} not found!", dto.getMerchantId()));
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
        loggerService.successLog(MessageFormat.format("Created acquirer with ID: {0}", a.getId()));
        return dto;
    }

    public Acquirer findByMerchantId(String merchantId) {
        loggerService.infoLog(MessageFormat.format("Finding acquirer by merchant ID: {0}", merchantId));
        for (Acquirer a: acquirerRepository.findAll()) {
            if (a.getMerchantId().equals(merchantId)) {
                loggerService.successLog(MessageFormat.format("Found acquirer with ID: {0}", a.getId()));
                return a;
            }
        }
        loggerService.errorLog(MessageFormat.format("Acquirer by merchant ID: {0} not found", merchantId));
        return null;
    }

    @Override
    public AcquirerDto registerQrCode(AcquirerDto dto) {
        Acquirer acquirer = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if(acquirer == null){
            acquirer  = new Acquirer();
            acquirer.setMerchantId(dto.getMerchantId());
            acquirer.setMerchantPassword(dto.getMerchantPassword());
            Bank bank = bankService.findByName(dto.getBank().getName());
            if (bank == null) {
                System.out.println("Bank " + dto.getBank().getName() + " not found");
                return null;
            }
            acquirer.setBank(bank);
            acquirer.setQrCodePayment(true);
        }else{
            acquirer.setQrCodePayment(true);
        }
        acquirerRepository.save(acquirer);

        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (a == null) {
            System.out.println("ACQUIRER with merchant id " + dto.getMerchantId() + " not found");
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
        return dto;
    }
}
