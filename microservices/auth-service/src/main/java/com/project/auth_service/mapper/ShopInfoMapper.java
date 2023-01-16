package com.project.auth_service.mapper;

import com.project.auth_service.dto.RegisterShopDto;
import com.project.auth_service.dto.ShopInfoDto;
import com.project.auth_service.model.WebShop;
import org.springframework.stereotype.Component;

@Component
public class ShopInfoMapper implements ModelToDtoMapper<WebShop, ShopInfoDto> {
    @Override
    public WebShop dtoToModel(ShopInfoDto shopInfoDto) {
        return null;
    }

    @Override
    public ShopInfoDto modelToDto(WebShop webShop) {
        ShopInfoDto dto=new ShopInfoDto();
        dto.setMail(webShop.getMail());
        dto.setName(webShop.getName());
        dto.setShopId(webShop.getShopId());
        dto.setCancelUrl(webShop.getCancelUrl());
        dto.setSuccessUrl(webShop.getSuccessUrl());
        dto.setReturnUrl(webShop.getReturnUrl());
        return dto;
    }
}
