package com.project.bank1.controller;

import com.project.bank1.dto.AcquirerDto;
import com.project.bank1.service.LoggerService;
import com.project.bank1.service.interfaces.AcquirerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/acquirers",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AcquirerController {
    @Autowired
    private AcquirerService acquirerService;
    private LoggerService loggerService = new LoggerService(this.getClass());


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<?> register(@RequestBody AcquirerDto dto){
        loggerService.infoLog(String.format("Registering new acquirer with merchant ID {} in bank {}", dto.getMerchantId(), dto.getBank().getName() ));
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

}
