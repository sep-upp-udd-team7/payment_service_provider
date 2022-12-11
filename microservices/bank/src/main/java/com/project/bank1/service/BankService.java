package com.project.bank1.service;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BankService {

    public RequestDto validateAcquirer(OnboardingRequestDto dto, String bankFrontendUrl) {
        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(dto.getMerchantId());
        // TODO SD: na osnovu merchant id -> izvuci merchant pass
        request.setMerchantPassword("paahakbudodwpjilsbdmmgjocmhwfxkxxwlxinwsvvigzaumeydpwtaacrsjqrjyuootdehhkvawrflqczhzhdsqfkraabsikuyz");
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(LocalDateTime.now());
        request.setSuccessUrl(bankFrontendUrl + "/success");
        request.setFailedUrl(bankFrontendUrl + "/failed");
        request.setErrorUrl(bankFrontendUrl + "/error");
        return request;
    }

}
