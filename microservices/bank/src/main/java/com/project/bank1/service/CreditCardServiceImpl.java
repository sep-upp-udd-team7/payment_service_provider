package com.project.bank1.service;

import com.project.bank1.dto.AcquirerResponseDto;
import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.enums.TransactionStatus;
import com.project.bank1.model.Acquirer;
import com.project.bank1.model.Transaction;
import com.project.bank1.service.interfaces.AcquirerService;
import com.project.bank1.service.interfaces.CreditCardService;
import com.project.bank1.service.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CreditCardServiceImpl implements CreditCardService {
    private LoggerService loggerService = new LoggerService(this.getClass());
    private static String validateIssuerEndpoint = "/accounts/validateAcquirer";
    @Autowired
    private Environment env;
    @Autowired
    private AcquirerService acquirerService;
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private WebClient webClient;

    @Override
    public RequestDto validateAcquirer(OnboardingRequestDto dto) throws Exception {
        String pspFrontendUrl = env.getProperty("psp.frontend");
        // TODO SD: na auth service-a izvuci merchant id
        Acquirer acquirer = acquirerService.findByMerchantId(dto.getMerchantId());
        if(acquirer == null) {
            String msg = "Merchant's credentials are incorrect or merchant is not registered";
            loggerService.invalidMerchantCredentials(dto.getMerchantId());
            throw new Exception(msg);
        }
        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(acquirer.getMerchantId());
        request.setMerchantPassword(acquirer.getMerchantPassword());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(dto.getMerchantTimestamp());
        request.setSuccessUrl(pspFrontendUrl + env.getProperty("psp.success-payment"));
        request.setFailedUrl(pspFrontendUrl + env.getProperty("psp.failed-payment"));
        request.setErrorUrl(pspFrontendUrl + env.getProperty("psp.error-payment"));
        return request;
    }

    @Override
    public AcquirerResponseDto startPayment(OnboardingRequestDto dto) throws Exception {
        RequestDto request = creditCardService.validateAcquirer(dto);
        Acquirer acquirer = acquirerService.findByMerchantId(dto.getMerchantId());
        Transaction transaction = transactionService.createTransaction(request, acquirer);

        ResponseEntity<AcquirerResponseDto> response = webClient.post()
                .uri(acquirer.getBank().getBankUrl() + validateIssuerEndpoint)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(AcquirerResponseDto.class)
                .block();
        if (!response.getStatusCode().is2xxSuccessful()) {
            transaction.setStatus(TransactionStatus.ERROR);
            transactionService.save(transaction);
            String errorPaymentUrl = env.getProperty("psp.frontend") + env.getProperty("psp.error-payment");
            throw new Exception(errorPaymentUrl);
        }

        transactionService.save(transaction);
        return getAcquirerResponseDtoWhenResponseIsSuccessful(response);
    }

    private AcquirerResponseDto getAcquirerResponseDtoWhenResponseIsSuccessful(ResponseEntity<AcquirerResponseDto> response) {
        AcquirerResponseDto responseDto = new AcquirerResponseDto();
        if (response.getBody() != null) {
            responseDto.setPaymentId(response.getBody().getPaymentId());
            responseDto.setPaymentUrl(response.getBody().getPaymentUrl());
            return responseDto;
        }
        System.out.println("Response AcquirerResponseDto body is null");
        return null;
    }

}