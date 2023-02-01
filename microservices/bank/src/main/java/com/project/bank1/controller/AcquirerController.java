package com.project.bank1.controller;

import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.dto.OperationResponse;
import com.project.bank1.service.LoggerService;
import com.project.bank1.service.interfaces.AcquirerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping(value = "/acquirers",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AcquirerController {
    @Autowired
    private AcquirerService acquirerService;
    private LoggerService loggerService = new LoggerService(this.getClass());


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> register(@RequestBody AcquirerDto dto){
        loggerService.infoLog(MessageFormat.format("Registering new acquirer with merchant ID {0} in bank {1}", dto.getMerchantId(), dto.getBank().getName() ));
        AcquirerDto response = acquirerService.register(dto);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registerQrCode")
    public ResponseEntity<?> registerQrCode(@RequestBody AcquirerDto dto){
        AcquirerDto response = acquirerService.registerQrCode(dto);
        if (response == null) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping("/remove-qrcode/{shopId}")
    public ResponseEntity<?> removeQrCode(@PathVariable String shopId){
        OperationResponse response=acquirerService.removeQrCode(shopId);
        if (response.isOperationResponse()){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/remove-bank-payment/{shopId}")
    public ResponseEntity<?> removeBankPayment(@PathVariable String shopId){
        OperationResponse response=acquirerService.removeBankPayment(shopId);
        if (response.isOperationResponse()){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
