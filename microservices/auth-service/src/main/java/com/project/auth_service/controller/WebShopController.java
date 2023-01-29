package com.project.auth_service.controller;

import com.project.auth_service.dto.*;
import com.project.auth_service.mapper.ShopInfoMapper;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.service.WebShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WebShopController {

    private final WebShopService webShopService;

    private final ShopInfoMapper shopInfoMapper;

    @GetMapping("web-shop-url/{shopId}")
    public WebShopUrl getWebShopUrl(@PathVariable String shopId){
        WebShop shop=webShopService.getShopById(shopId);
        WebShopUrl webShopUrl=new WebShopUrl();
        webShopUrl.setReturnUrl(shop.getReturnUrl());
        webShopUrl.setCancelUrl(shop.getCancelUrl());
        webShopUrl.setSuccessUrl(shop.getSuccessUrl());
        return webShopUrl;
    }

    @PostMapping("/register-shop")
    public RegisterShopResponse registerShop(@RequestBody RegisterShopDto dto){
        return webShopService.registerShop(dto);
    }

    @PostMapping("/add-payment-method")
    public ResponseEntity<OperationResponse> addPaymentMethod(@RequestBody AddPaymentMethodDto dto){
        OperationResponse response=webShopService.addPaymentMethod(dto);
        if (response.isOperationResponse()){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/remove-payment-method")
    public ResponseEntity<OperationResponse> removePaymentMethod(@RequestBody RemovePaymentMethodDto dto){
        OperationResponse response=webShopService.removePaymentMethod(dto);
        if (response.isOperationResponse()){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto dto){
        LoginResponse response=webShopService.loginWebShop(dto);
        if (response.getToken()!=null){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/shops/{id}")
    public ResponseEntity<ShopInfoDto> getShop(@PathVariable String id){
        WebShop shop=webShopService.getShopById(id);
        if (shop!=null){
            ShopInfoDto dto=shopInfoMapper.modelToDto(shop);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
