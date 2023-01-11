package com.project.bank1.controller;

import com.project.bank1.service.LoggerService;
import com.project.bank1.service.interfaces.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/banks", produces = MediaType.APPLICATION_JSON_VALUE)
public class BankController {
    private LoggerService loggerService = new LoggerService(this.getClass());
    @Autowired
    private BankService bankService;


    @RequestMapping(method = RequestMethod.GET, value = "/getAll")
    public ResponseEntity<?> getAll() {
        loggerService.infoLog("Getting all banks from bank microservice");
        return new ResponseEntity<>(bankService.getAll(), HttpStatus.OK);
    }
}
