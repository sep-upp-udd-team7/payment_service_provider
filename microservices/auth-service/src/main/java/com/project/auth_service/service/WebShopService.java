package com.project.auth_service.service;

import com.project.auth_service.model.WebShop;
import com.project.auth_service.repository.WebShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebShopService {

    private final WebShopRepository shopRepository;

    public WebShop getShopById(String shopId){
        return shopRepository.getShopById(shopId);
    }
}
