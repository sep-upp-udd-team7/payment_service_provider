package com.project.paypal.service;

import com.project.paypal.dto.SubscribeWebShopDto;
import com.project.paypal.model.SubscribedWebShop;
import com.project.paypal.repository.SubscribedWebShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeWebShopServiceImpl {

    private final SubscribedWebShopRepository subscribedWebShopRepository;
    public boolean subscribeWebShop(SubscribeWebShopDto dto) {
        if (subscribedWebShopRepository.getByShopId(dto.getShopId())==null){
            SubscribedWebShop shop=new SubscribedWebShop();
            shop.setShopId(dto.getShopId());
            shop.setClientId(dto.getPaymentApiClientId());
            shop.setClientSecret(dto.getPaymentApiSecret());
            subscribedWebShopRepository.save(shop);
            return true;
        }
        return false;
    }

    public boolean unsubscribeWebShop(String shopId){
        SubscribedWebShop shop= subscribedWebShopRepository.getByShopId(shopId);
        if (shop!=null){
            subscribedWebShopRepository.delete(shop);
            return true;
        }
        return false;
    }
}
