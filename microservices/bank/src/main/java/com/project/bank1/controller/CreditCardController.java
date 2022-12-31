package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.service.LoggerService;
import com.project.bank1.service.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/credit-cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditCardController {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private CreditCardService creditCardService;


    @RequestMapping(method = RequestMethod.POST, value = "/onboarding")
    public ResponseEntity<?> onboarding(@RequestBody OnboardingRequestDto dto){
        loggerService.validateAcquirer(dto.getMerchantId(), dto.getMerchantOrderId());
        try {
            return new ResponseEntity<>(creditCardService.startPayment(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
