package com.project.auth_service.mapper;

import com.project.auth_service.dto.PaymentMethodDto;
import com.project.auth_service.model.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper implements ModelToDtoMapper<PaymentMethod, PaymentMethodDto> {
    @Override
    public PaymentMethod dtoToModel(PaymentMethodDto paymentMethodDto) {
        return null;
    }

    @Override
    public PaymentMethodDto modelToDto(PaymentMethod paymentMethod) {
        PaymentMethodDto dto=new PaymentMethodDto();
        dto.setName(paymentMethod.getName());
        return dto;
    }
}
