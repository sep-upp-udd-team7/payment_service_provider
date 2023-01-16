package com.project.crypto.service;

import com.project.crypto.dto.SubscribeWebShopDto;
import com.project.crypto.model.Merchant;
import com.project.crypto.repository.MerchantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeWebShopService {

    private final MerchantRepository merchantRepository;
    public boolean subscribeWebShop(SubscribeWebShopDto dto) {
        Merchant merchant= merchantRepository.findByMerchantId(dto.getShopId());
        if (merchant==null){
            Merchant newMerchant=new Merchant();
            newMerchant.setMerchantId(dto.getShopId());
            newMerchant.setToken(dto.getPaymentApiClientId());
            merchantRepository.save(newMerchant);
            return true;
        }
        return false;
    }

    public boolean unsubscribeWebShop(String shopId) {
        Merchant merchant=merchantRepository.findByMerchantId(shopId);
        if (merchant!=null){
            merchantRepository.delete(merchant);
            return true;
        }
        return false;
    }
}
