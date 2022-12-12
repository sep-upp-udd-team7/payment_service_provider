package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.AcquirerResponseDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.model.Acquirer;
import com.project.bank1.service.AcquirerService;
import com.project.bank1.service.CreditCardService;
import com.project.bank1.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
    private CreditCardService creditCardService;
    @Autowired
    private AcquirerService acquirerService;

    private LoggerService loggerService = new LoggerService(this.getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/onboarding")
    public ResponseEntity<?> validate(@RequestBody OnboardingRequestDto dto){
        try {
            RequestDto request = creditCardService.validateAcquirer(dto);
            loggerService.validateAcquirer(dto.getMerchantId(), dto.getMerchantOrderId());
            Acquirer acquirer = acquirerService.findByMerchantId(dto.getMerchantId());

            ResponseEntity<AcquirerResponseDto> response = webClient.post()
                    .uri(acquirer.getBank().getBankUrl() + validateIssuerEndpoint)
                    .body(BodyInserters.fromValue(request))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(AcquirerResponseDto.class)
                    .block();

            if (response.getStatusCode().is2xxSuccessful()) {
                AcquirerResponseDto responseDto = new AcquirerResponseDto();
                responseDto.setPaymentId(response.getBody().getPaymentId());
                responseDto.setPaymentUrl(response.getBody().getPaymentUrl());
                return new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                // TODO SD: ovo ispraviti kada se dodaju stranice na frontu
                String pspFrontendUrl = environment.getProperty("psp.frontend") + "/error";
                System.out.println(pspFrontendUrl);
                AcquirerResponseDto responseDto = new AcquirerResponseDto();
                if (response.getStatusCode() != null) {
                    responseDto.setPaymentId(response.getBody().getPaymentId());
                }
                responseDto.setPaymentUrl(pspFrontendUrl);
                return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
