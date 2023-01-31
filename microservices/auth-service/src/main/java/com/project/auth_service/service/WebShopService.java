package com.project.auth_service.service;

import com.project.auth_service.dto.*;
import com.project.auth_service.model.PaymentMethod;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.repository.PaymentMethodRepository;
import com.project.auth_service.repository.WebShopRepository;
import com.project.auth_service.utils.JwtUtil;
import com.project.auth_service.utils.RandomCharacterGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WebShopService {

    private final WebShopRepository shopRepository;

    private final JwtUtil jwtUtil;
    private final PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private SecretGenerator secretGenerator;


    @Autowired
    private CodeVerifier verifier;


    public WebShop getShopById(String shopId) {
        return shopRepository.getShopById(shopId);
    }

    public RegisterShopResponse registerShop(RegisterShopDto registerShopDto) {
        RegisterShopResponse response = new RegisterShopResponse();
        WebShop shop = new WebShop();
        shop.setShopId(RandomCharacterGenerator.generateURLSafeString(16));
        shop.setShopSecret(RandomCharacterGenerator.generateURLSafeString(16));
        shop.setName(registerShopDto.getName());
        shop.setMail(registerShopDto.getMail());
        shop.setPassword(new BCryptPasswordEncoder().encode(registerShopDto.getPassword()));
        shop.setSuccessUrl(registerShopDto.getSuccessUrl());
        shop.setReturnUrl(registerShopDto.getReturnUrl());
        shop.setCancelUrl(registerShopDto.getCancelUrl());
        shop.setUsing2FA(registerShopDto.getUsing2FA());
        if (shop.getUsing2FA()) {
            shop.setTwoFAsecret(secretGenerator.generate());
        }
        shopRepository.save(shop);
        response.setShopSecret(shop.getShopSecret());
        response.setShopId(shop.getShopId());
        response.setSecret(shop.getTwoFAsecret());
        response.setUsing2FA(shop.getUsing2FA());
        return response;
    }

    public OperationResponse addPaymentMethod(AddPaymentMethodDto dto){
        PaymentMethod paymentMethod=paymentMethodRepository.get(dto.getPaymentMethodId());
        if (paymentMethod.getName().equalsIgnoreCase("BANK") || paymentMethod.getName().equalsIgnoreCase("QR_CODE")){
            WebShop shop=shopRepository.getShopById(dto.getShopId());
            Set<PaymentMethod> paymentMethodSet=shop.getPaymentMethods();
            paymentMethodSet.add(paymentMethod);
            shop.setPaymentMethods(paymentMethodSet);
            shopRepository.save(shop);
            OperationResponse response=new OperationResponse();
            response.setOperationResponse(true);
            return response;
        }
        WebClient webClient = WebClient.create();

        SubscribeWebShopToPaymentMethodRequest requestDto=new SubscribeWebShopToPaymentMethodRequest();
        requestDto.setShopId(dto.getShopId());
        requestDto.setPaymentApiClientId(dto.getPaymentApiClientId());
        requestDto.setPaymentApiSecret(dto.getPaymentApiSecret());
        OperationResponse response = webClient.post()
                .uri(paymentMethod.getAddPaymentMethodUrl())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(requestDto), SubscribeWebShopToPaymentMethodRequest.class)
                .retrieve()
                .bodyToMono(OperationResponse.class)
                .block();

        if (response.isOperationResponse()){
            WebShop shop=shopRepository.getShopById(dto.getShopId());
            Set<PaymentMethod> paymentMethodSet=shop.getPaymentMethods();
            paymentMethodSet.add(paymentMethod);
            shop.setPaymentMethods(paymentMethodSet);
            shopRepository.save(shop);
        }

        return response;

    }

    public OperationResponse removePaymentMethod(RemovePaymentMethodDto dto){
        PaymentMethod paymentMethod=paymentMethodRepository.get(dto.getPaymentMethodId());
        WebClient webClient = WebClient.create();

        String serviceUrl=resolveServiceUrl(paymentMethod);
        SubscribeWebShopToPaymentMethodRequest requestDto=new SubscribeWebShopToPaymentMethodRequest();
        requestDto.setShopId(dto.getShopId());

        OperationResponse response = webClient.post()
                .uri(serviceUrl+"/"+dto.getShopId())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(requestDto), RemovePaymentMethodDto.class)
                .retrieve()
                .bodyToMono(OperationResponse.class)
                .block();
        if (response.isOperationResponse()){
            WebShop shop=shopRepository.getShopById(dto.getShopId());
            Set<PaymentMethod> paymentMethodSet=shop.getPaymentMethods();
            paymentMethodSet.remove(paymentMethod);
            shop.setPaymentMethods(paymentMethodSet);
            shopRepository.save(shop);
        }

        return response;

    }

    public LoginResponse loginWebShop(LoginDto dto){
        WebShop webShop=shopRepository.getShopByMail(dto.getMail());
        if (webShop!=null){

            if (webShop.getUsing2FA()) {
                if(!verifier.isValidCode(webShop.getTwoFAsecret(), dto.getCode())){
                    return null;
                }
            }

            String token = null;
            if (new BCryptPasswordEncoder().matches(dto.getPassword(), webShop.getPassword())) {
                token =jwtUtil.createToken(webShop.getShopId());
                LoginResponse loginResponse=new LoginResponse();
                loginResponse.setToken(token);
                loginResponse.setShopId(webShop.getShopId());
                return loginResponse;
            }
        }
        LoginResponse loginResponse=new LoginResponse();
        loginResponse.setToken(null);
        return loginResponse;
    }

    private String resolveServiceUrl(PaymentMethod paymentMethod) {
        switch (paymentMethod.getName()){
            case "PAYPAL": return "http://localhost:8084/unsubscribe-web-shop";
            case "BANK": return "http://localhost:8080/acquirers/remove-bank-payment";
            case "QR_CODE": return "http://localhost:8080/acquirers/remove-qrcode";
            case "CRYPTO": return "http://localhost:8082/unsubscribe-web-shop";
        }
        return "";
    }

    private WebShop getShop(String shopId){
        return shopRepository.getShopById(shopId);
    }

    public Boolean checkIfEnabled2FA(String username) throws Exception {

        WebShop web = shopRepository.getShopByMail(username);
        if(web == null){ // || !user.getIsActivated()
//            log.error("Check if 2FA is enabled for account failed. Account with username " + username + " not activated.");
            throw new Exception("Web shop with username " + username + " not activated.");
        }

        return web.getUsing2FA();
    }

}
