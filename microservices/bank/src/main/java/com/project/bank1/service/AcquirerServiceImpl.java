package com.project.bank1.service;

import com.project.bank1.dto.AcquirerDto;/
import com.project.bank1.dto.MerchantCredentialsDto;
import com.project.bank1.dto.OperationResponse;
import com.project.bank1.mapper.BankMapper;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Bank;
import com.project.bank1.repository.AcquirerRepository;
import com.project.bank1.service.interfaces.AcquirerService;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;

@Service
public class AcquirerServiceImpl implements AcquirerService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private BankService bankService;
    @Autowired
    private AcquirerRepository acquirerRepository;
    @Autowired
    private Environment env;

    public AcquirerDto register(AcquirerDto dto) {
        loggerService.infoLog(MessageFormat.format("Registering acquirer with merchant ID: {0} and merchant password: {1}",
                dto.getMerchantId(), dto.getMerchantPassword()));

        ResponseEntity<String> bankResponse = sendRequestForApiKeyToBank(dto);
        if (!bankResponse.getStatusCode().is2xxSuccessful()) {
            loggerService.errorLog("Invalid merchant credentials or bank is unavailable");
            return null;
        }

        Acquirer acquirer = acquirerRepository.findByMerchantId(dto.getMerchantId());
        if (acquirer == null) {
            acquirer = new Acquirer();
            acquirer.setMerchantId(dto.getMerchantId());
            
        Acquirer acquirer = acquirerRepository.getByShopId(dto.getShopId());
        if(acquirer == null){
            acquirer = new Acquirer();
            acquirer.setMerchantId(dto.getMerchantId());
            acquirer.setShopId(dto.getShopId());
            acquirer.setBankPayment(true);
            // TODO SD: base64 encode?
            
            acquirer.setMerchantPassword(dto.getMerchantPassword());
            acquirer.setApiKey(bankResponse.getBody());
            Bank bank = bankService.findByName(dto.getBank().getName());
            if (bank == null) {
                loggerService.errorLog(MessageFormat.format("Bank with name {0} not found!", dto.getBank().getName()));
                return null;
            }
            acquirer.setBank(bank);
            acquirerRepository.save(acquirer);
        }
        Acquirer a = acquirerRepository.findByMerchantId(dto.getMerchantId());

        Acquirer a = acquirerRepository.getByShopId(dto.getShopId());
        if (a == null) {
            loggerService.errorLog(MessageFormat.format("Acquirer with merchant ID {0} not found!", dto.getMerchantId()));
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
        loggerService.successLog(MessageFormat.format("Created acquirer with ID: {0}", a.getId()));
        return dto;
    }

    private ResponseEntity<String> sendRequestForApiKeyToBank(AcquirerDto dto) {
        String bankBackendUrl = bankService.findByName(dto.getBank().getName()).getBankUrl() + env.getProperty("bank.access-token");
        loggerService.infoLog(MessageFormat.format("Sending request to bank with URL: {0}", bankBackendUrl));

        ResponseEntity<String> bankResponse = WebClient.builder()
                .build().post()
                .uri(bankBackendUrl)
                .body(BodyInserters.fromValue(getMerchantCredentials(dto)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class)
                .block();
        return bankResponse;
    }

    private MerchantCredentialsDto getMerchantCredentials(AcquirerDto acquirer) {
        MerchantCredentialsDto dto = new MerchantCredentialsDto();
        dto.setMerchantId(acquirer.getMerchantId());
        dto.setMerchantPassword(acquirer.getMerchantPassword());
        dto.setBankName(acquirer.getBank().getName());
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
        Acquirer acquirer = acquirerRepository.getByShopId(dto.getShopId());
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

        Acquirer a = acquirerRepository.getByShopId(dto.getShopId());
        if (a == null) {
            System.out.println("ACQUIRER with merchant id " + dto.getMerchantId() + " not found");
            return null;
        }
        dto.setId(a.getId());
        dto.setBank(new BankMapper().mapModelToDto(a.getBank()));
        return dto;
    }

    @Override
    public OperationResponse removeQrCode(String shopId) {
        Acquirer acquirer=acquirerRepository.getByShopId(shopId);
        if (acquirer!=null){
            acquirer.setQrCodePayment(false);
            acquirerRepository.save(acquirer);
            if (!acquirer.getBankPayment()){
                acquirerRepository.delete(acquirer);
            }
            OperationResponse response=new OperationResponse();
            response.setOperationResponse(true);
            return response;
        }
        OperationResponse response=new OperationResponse();
        response.setOperationResponse(false);
        return response;
    }

    public OperationResponse removeBankPayment(String shopId){
        Acquirer acquirer=acquirerRepository.getByShopId(shopId);
        if (acquirer!=null){
            if (!acquirer.getQrCodePayment()){
                acquirerRepository.delete(acquirer);
                OperationResponse response=new OperationResponse();
                response.setOperationResponse(true);
                return response;
            }
            acquirer.setBankPayment(false);
            acquirerRepository.save(acquirer);
            OperationResponse response=new OperationResponse();
            response.setOperationResponse(true);
            return response;
        }
        OperationResponse response=new OperationResponse();
        response.setOperationResponse(false);
        return response;
    }
}
