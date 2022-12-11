package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.service.BankService;
import com.project.bank1.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class BankController {
    private static String validateIssuerEndpoint = "/accounts/validateAcquirer";
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment environment;
    @Autowired
    private BankService bankService;

    private LoggerService loggerService = new LoggerService(this.getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/credit-card")
    public String validate(@RequestBody OnboardingRequestDto dto){
        String bankBackendUrl = environment.getProperty("bank-application.backend");
        String bankFrontendUrl = environment.getProperty("bank-application.frontend");

        RequestDto request = bankService.validateAcquirer(dto, bankFrontendUrl);
        loggerService.validateAcquirer(dto.getMerchantId(), dto.getMerchantOrderId());

        ResponseEntity<String> response = webClient.post()
                .uri( bankBackendUrl + validateIssuerEndpoint)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .toEntity(String.class)
                .block();

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return "Error!";
        }
    }

}
