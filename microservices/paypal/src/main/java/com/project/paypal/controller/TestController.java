package com.project.paypal.controller;

import com.project.paypal.dto.CreatePaymentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<CreatePaymentDto> test() {
        CreatePaymentDto dto=new CreatePaymentDto();
        dto.setAmount("5820");
        return new ResponseEntity<CreatePaymentDto>(dto, HttpStatus.OK);
    }
}
