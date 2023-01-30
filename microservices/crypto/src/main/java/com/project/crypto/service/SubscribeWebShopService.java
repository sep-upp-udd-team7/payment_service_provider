package com.project.crypto.service;

import com.project.crypto.dto.SubscribeWebShopDto;
import com.project.crypto.model.Merchant;
import com.project.crypto.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeWebShopService {

    private LoggerService loggerService = new LoggerService(this.getClass());

    private final MerchantRepository merchantRepository;
    public boolean subscribeWebShop(SubscribeWebShopDto dto) {
        Merchant merchant= merchantRepository.findByMerchantId(dto.getShopId());
        if (merchant==null){
            Merchant newMerchant=new Merchant();
            newMerchant.setMerchantId(dto.getShopId());
            newMerchant.setToken(dto.getPaymentApiClientId());
            merchantRepository.save(newMerchant);
            loggerService.successLog("Successfully subscribed web shop with id: "+dto.getShopId());
            return true;
        }
        loggerService.errorLog("Web shop with id: "+dto.getShopId()+" already exists");
        return false;
    }

    public boolean unsubscribeWebShop(String shopId) {
        Merchant merchant=merchantRepository.findByMerchantId(shopId);
        if (merchant!=null){
            merchantRepository.delete(merchant);
            loggerService.successLog("Successfully unsubscribed web shop with id: "+shopId);
            return true;
        }
        loggerService.errorLog("Web shop with id: "+shopId+" does not exist");
        return false;
    }
}
