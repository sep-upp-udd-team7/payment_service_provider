package com.project.auth_service.controller;

import com.project.auth_service.dto.WebShopUrl;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.service.WebShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebShopController {

    private final WebShopService webShopService;

    @GetMapping("web-shop-url/{shopId}")
    public WebShopUrl getWebShopUrl(@PathVariable String shopId){
        WebShop shop=webShopService.getShopById(shopId);
        WebShopUrl webShopUrl=new WebShopUrl();
        webShopUrl.setReturnUrl(shop.getReturnUrl());
        webShopUrl.setCancelUrl(shop.getCancelUrl());
        webShopUrl.setSuccessUrl(shop.getSuccessUrl());
        return webShopUrl;
    }

}
