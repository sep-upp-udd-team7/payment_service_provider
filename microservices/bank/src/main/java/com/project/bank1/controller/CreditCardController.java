package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.AcquirerResponseDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.service.BankService;
import com.project.bank1.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping(value = "/credit-cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditCardController {
    private static String validateIssuerEndpoint = "/accounts/validateAcquirer";
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;
    @Autowired
    private BankService bankService;

    private LoggerService loggerService = new LoggerService(this.getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/onboarding")
    public AcquirerResponseDto validate(@RequestBody OnboardingRequestDto dto){
        String bankBackendUrl = environment.getProperty("bank-application.backend");
        String bankFrontendUrl = environment.getProperty("bank-application.frontend");

        RequestDto request = bankService.validateAcquirer(dto, bankFrontendUrl);
        loggerService.validateAcquirer(dto.getMerchantId(), dto.getMerchantOrderId());

        ResponseEntity<AcquirerResponseDto> response = webClient.post()
                .uri( bankBackendUrl + validateIssuerEndpoint)
                .body(BodyInserters.fromValue(request))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(AcquirerResponseDto.class)
                .block();

        if (response.getStatusCode().is2xxSuccessful()) {
            AcquirerResponseDto responseDto = new AcquirerResponseDto();
            responseDto.setPaymentId(response.getBody().getPaymentId());
            responseDto.setPaymentUrl(response.getBody().getPaymentUrl());
            return responseDto;
        } else {
            String paymentUrl = environment.getProperty("bank-service.frontend") + "/bank-error";
            System.out.println(paymentUrl);
            AcquirerResponseDto responseDto = new AcquirerResponseDto();
            if (response.getStatusCode() != null) {
                responseDto.setPaymentId(response.getBody().getPaymentId());
            }
            responseDto.setPaymentUrl(paymentUrl);
            return responseDto;
        }
    }

}
