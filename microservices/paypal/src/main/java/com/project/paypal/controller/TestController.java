package com.project.paypal.controller;

import com.project.paypal.dto.CreatePaymentDto;
import com.project.paypal.service.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private LoggerService loggerService = new LoggerService(this.getClass());

    @GetMapping("/test")
    public ResponseEntity<CreatePaymentDto> test() {
        loggerService.test("1234");

        CreatePaymentDto dto=new CreatePaymentDto();
        dto.setAmount("5820");
        return new ResponseEntity<CreatePaymentDto>(dto, HttpStatus.OK);
    }
}
