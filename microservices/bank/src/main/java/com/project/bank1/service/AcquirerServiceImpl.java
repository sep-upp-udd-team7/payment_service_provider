package com.project.bank1.service;

import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.mapper.BankMapper;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.AcquirerRepository;
import com.project.bank1.service.interfaces.AcquirerService;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirerServiceImpl implements AcquirerService {
    @Autowired
    private BankService bankService;
    @Autowired
    private AcquirerRepository acquirerRepository;

    public AcquirerDto register(AcquirerDto dto) {
        //TODO KS: dodati provjeru ako vec postoji acquirer da ga ne dodaje opet jer je automatski dodat na banku prilikom dodavanja qr code opcije
        Acquirer acquirer = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if(acquirer == null){
            acquirer = new Acquirer();
            acquirer.setMerchantId(dto.getMerchantId());
            acquirer.setMerchantPassword(dto.getMerchantPassword());
            Bank bank = bankService.findByName(dto.getBank().getName());
            if (bank == null) {
                System.out.println("Bank " + dto.getBank().getName() + " not found");
                return null;
            }
            acquirer.setBank(bank);
            acquirerRepository.save(acquirer);
        }

        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (a == null) {
            System.out.println("ACQUIRER with merchant id " + dto.getMerchantId() + " not found");
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
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
