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
    public ResponseEntity<?> onboarding(@RequestBody OnboardingRequestDto dto){
        try {
            // TODO SD: kreirati transakciju
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
                return new ResponseEntity<>(getAcquirerResponseDtoWhenResponseIsSuccessful(response), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(getAcquirerResponseDtoWhenResponseIsUnsuccessful(response), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private AcquirerResponseDto getAcquirerResponseDtoWhenResponseIsUnsuccessful(ResponseEntity<AcquirerResponseDto> response) {
        String pspFrontendUrl = environment.getProperty("psp.frontend");
        String errorPaymentUrl = pspFrontendUrl + environment.getProperty("psp.error-payment");
        System.out.println(errorPaymentUrl);
        AcquirerResponseDto responseDto = new AcquirerResponseDto();
        if (response.getStatusCode() != null) {
            responseDto.setPaymentId(response.getBody().getPaymentId());
        }
        responseDto.setPaymentUrl(errorPaymentUrl);
        return responseDto;
    }

    private AcquirerResponseDto getAcquirerResponseDtoWhenResponseIsSuccessful(ResponseEntity<AcquirerResponseDto> response) {
        AcquirerResponseDto responseDto = new AcquirerResponseDto();
        responseDto.setPaymentId(response.getBody().getPaymentId());
        responseDto.setPaymentUrl(response.getBody().getPaymentUrl());
        return responseDto;
    }

}
