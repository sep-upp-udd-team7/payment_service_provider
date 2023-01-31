package com.project.bank1.controller;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.ResponseDto;
import com.project.bank1.service.LoggerService;
import com.project.bank1.service.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping(value = "/credit-cards", produces = MediaType.APPLICATION_JSON_VALUE)
public class CreditCardController {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private CreditCardService creditCardService;


    @RequestMapping(method = RequestMethod.POST, value = "/startPayment")
    public ResponseEntity<?> startPayment(@RequestBody OnboardingRequestDto dto){
        loggerService.infoLog(MessageFormat.format("Start payment by validating acquirer. Shop ID: {0} with order ID: {1}",
                dto.getShopId(), dto.getMerchantOrderId()));
        try {
            return new ResponseEntity<>(creditCardService.startPayment(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/finishPayment")
    public ResponseEntity<?> finishPayment(@RequestBody ResponseDto dto){
        loggerService.infoLog(MessageFormat.format("Finishing payment by validating acquirer. Merchant order ID: {0}",
                dto.getMerchantOrderId()));
        try {
            return new ResponseEntity<String>(creditCardService.finishPayment(dto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
