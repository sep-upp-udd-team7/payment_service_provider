package com.project.auth_service.service;

import com.project.auth_service.dto.PaymentMethodDto;
import com.project.auth_service.dto.PaymentMethodFrontDto;
import com.project.auth_service.model.PaymentMethod;
import com.project.auth_service.model.WebShop;
import com.project.auth_service.repository.PaymentMethodRepository;
import com.project.auth_service.repository.WebShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    private final WebShopRepository webShopRepository;
    public List<PaymentMethodFrontDto> generatePaymentMethodsForFront(String shopId){
        List<PaymentMethod> paymentMethods=paymentMethodRepository.findAll();
        WebShop shop=webShopRepository.getShopById(shopId);
        List<PaymentMethod> subscribedPaymentMethods=new ArrayList<>(shop.getPaymentMethods());
        List<PaymentMethodFrontDto> result=new ArrayList<>();
        for(PaymentMethod method:paymentMethods){
            if (existInSubscribedMethods(method,subscribedPaymentMethods)){
                PaymentMethodFrontDto frontDto=new PaymentMethodFrontDto();
                frontDto.setId(method.getId());
                frontDto.setName(method.getName());
                frontDto.setCanSubscribe(false);
                result.add(frontDto);
            }else{
                PaymentMethodFrontDto frontDto=new PaymentMethodFrontDto();
                frontDto.setId(method.getId());
                frontDto.setName(method.getName());
                frontDto.setCanSubscribe(true);
                result.add(frontDto);
            }
        }
        return result;
    }

    private boolean existInSubscribedMethods(PaymentMethod method, List<PaymentMethod> subscribedPaymentMethods) {
        for(PaymentMethod subscribedPaymentMethod:subscribedPaymentMethods){
            if (subscribedPaymentMethod.getId()==method.getId()){
                return true;
            }
        }
        return false;
    }


}
