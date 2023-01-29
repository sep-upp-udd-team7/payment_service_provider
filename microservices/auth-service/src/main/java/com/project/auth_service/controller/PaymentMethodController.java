package com.project.auth_service.controller;

import com.project.auth_service.dto.PaymentMethodDto;
import com.project.auth_service.dto.PaymentMethodFrontDto;
import com.project.auth_service.mapper.PaymentMethodMapper;
import com.project.auth_service.model.PaymentMethod;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.service.PaymentMethodService;
import com.project.auth_service.service.WebShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentMethodController {

    private final WebShopService shopService;
    private final PaymentMethodMapper paymentMethodMapper;

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/payment-methods/{shopId}")
    public List<PaymentMethodDto> getShopPaymentMethods(@PathVariable String shopId){
        WebShop shop= shopService.getShopById(shopId);
        List<PaymentMethod> methods=new ArrayList<>(shop.getPaymentMethods());
        List<PaymentMethodDto> dtos=paymentMethodMapper.modelToDto(methods);
        return dtos;
    }

    @GetMapping("/profile-payment-options/{id}")
    public ResponseEntity<List<PaymentMethodFrontDto>> getPaymentMethods(@PathVariable String id){
        return new ResponseEntity<>(paymentMethodService.generatePaymentMethodsForFront(id), HttpStatus.OK);
    }
}
