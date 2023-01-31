package com.project.auth_service.controller;

import com.project.auth_service.dto.*;
import com.project.auth_service.mapper.ShopInfoMapper;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.service.WebShopService;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrDataFactory;
import dev.samstevens.totp.qr.QrGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@RestController
@RequiredArgsConstructor
public class WebShopController {

    private final WebShopService webShopService;

    private final ShopInfoMapper shopInfoMapper;

    @Autowired
    private QrDataFactory qrDataFactory;

    @Autowired
    private QrGenerator qrGenerator;

    @Autowired
    private CodeVerifier verifier;



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
    public RegisterShopResponse registerShop(@RequestBody RegisterShopDto dto) throws QrGenerationException {
        RegisterShopResponse res = webShopService.registerShop(dto);
        if (dto.getUsing2FA()) {
            QrData data = qrDataFactory.newBuilder().label(dto.getMail()).secret(res.getSecret()).issuer("PSP").build();
            String qrCodeImage = getDataUriForImage(qrGenerator.generate(data), qrGenerator.getImageMimeType());
            res.setQrCode(qrCodeImage);
        }
        return res;
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
        if(response != null){
            if (response.getToken() != null){
                return new ResponseEntity<>(response,HttpStatus.OK);
            }
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


    @RequestMapping(method = RequestMethod.GET, value = "/checkIfEnabled2FA/{username}")
    public ResponseEntity<?> checkIfEnabled2FA(@PathVariable String username) throws Exception {

        try{
            Boolean isEnabled2FA = webShopService.checkIfEnabled2FA(username);

//            log.info("Check if 2FA is enabled for account success for username: " + username);
            return new ResponseEntity(isEnabled2FA, HttpStatus.OK);

        }catch (Exception e){
//            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
