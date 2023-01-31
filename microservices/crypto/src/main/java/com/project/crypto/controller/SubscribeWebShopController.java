package com.project.crypto.controller;

import com.project.crypto.dto.OperationResponse;
import com.project.crypto.dto.SubscribeWebShopDto;
import com.project.crypto.service.SubscribeWebShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscribeWebShopController {

    private final SubscribeWebShopService subscribeWebShopService;
    @PostMapping("/subscribe-web-shop")
    public OperationResponse subscribeWebShop(@RequestBody SubscribeWebShopDto dto){
        boolean ret= subscribeWebShopService.subscribeWebShop(dto);
        OperationResponse response=new OperationResponse();
        response.setOperationResponse(ret);
        return response;
    }

    @PostMapping("/unsubscribe-web-shop/{shopId}")
    public OperationResponse unsubscribeWebShop(@PathVariable String shopId){
        boolean ret= subscribeWebShopService.unsubscribeWebShop(shopId);
        OperationResponse response=new OperationResponse();
        response.setOperationResponse(ret);
        return response;
    }
}

