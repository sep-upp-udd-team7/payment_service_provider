package com.project.paypal.controller;

import com.project.paypal.dto.SubscribeWebShopDto;
import com.project.paypal.dto.SubscribeWebShopResponse;
import com.project.paypal.service.SubscribeWebShopServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscribeWebShopController {

    private final SubscribeWebShopServiceImpl subscribeWebShopService;
    @PostMapping("/subscribe-web-shop")
    public SubscribeWebShopResponse subscribeWebShop(@RequestBody SubscribeWebShopDto dto){
        boolean ret= subscribeWebShopService.subscribeWebShop(dto);
        SubscribeWebShopResponse response=new SubscribeWebShopResponse();
        response.setOperationResponse(ret);
        return response;
    }

    @PostMapping("/unsubscribe-web-shop/{shopId}")
    public SubscribeWebShopResponse unsubscribeWebShop(@PathVariable String shopId){
        boolean ret= subscribeWebShopService.unsubscribeWebShop(shopId);
        SubscribeWebShopResponse response=new SubscribeWebShopResponse();
        response.setOperationResponse(ret);
        return response;
    }
}
