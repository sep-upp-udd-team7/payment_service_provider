package com.project.bank1.service;

import com.project.bank1.dto.BankDto;
import com.project.bank1.mapper.BankMapper;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.BankRepository;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private BankRepository bankRepository;

    public Bank findByName(String name) {
        for (Bank b: bankRepository.findAll()) {
            if (b.getName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    public List<BankDto> getAll() {
        List<BankDto> dtos = new ArrayList<>();
        for (Bank b: bankRepository.findAll()) {
            dtos.add(new BankMapper().mapModelToDto(b));
        }
        return dtos;
    }
}
