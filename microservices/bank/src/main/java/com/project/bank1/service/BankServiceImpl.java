package com.project.bank1.service;

import com.project.bank1.dto.BankDto;
import com.project.bank1.mapper.BankMapper;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.BankRepository;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private BankRepository bankRepository;

    public Bank findByName(String name) {
        loggerService.infoLog(MessageFormat.format("Finding bank by bank name: {0}", name));
        for (Bank b: bankRepository.findAll()) {
            if (b.getName().equals(name)) {
                loggerService.successLog(MessageFormat.format("Found bank with ID: {0} by name: {1}", b.getId(), b.getName()));
                return b;
            }
        }
        loggerService.errorLog(MessageFormat.format("Not found bank with name: ", name));
        return null;
    }

    public List<BankDto> getAll() {
        loggerService.infoLog("Get all banks from Payment service provider");
        List<BankDto> dtos = new ArrayList<>();
        for (Bank b: bankRepository.findAll()) {
            dtos.add(new BankMapper().mapModelToDto(b));
        }
        loggerService.debugLog(MessageFormat.format("Found {0} banks in Payment service provider", dtos.size()));
        return dtos;
    }
}
