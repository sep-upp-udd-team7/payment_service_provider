package com.project.bank1.service;

import com.project.bank1.dto.OnboardingRequestDto;
import com.project.bank1.dto.RequestDto;
import com.project.bank1.model.Acquirer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreditCardService {
    @Autowired
    private Environment environment;
    @Autowired
    private AcquirerService acquirerService;

    public RequestDto validateAcquirer(OnboardingRequestDto dto) throws Exception {
        String pspFrontendUrl = environment.getProperty("psp.frontend");
        Acquirer acquirer = acquirerService.findByMerchantId(dto.getMerchantId());
        if(acquirer == null) {
            throw new Exception("Merchant's credentials are incorrect or do not exist");
        }
        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(dto.getMerchantId());
        request.setMerchantPassword(acquirer.getMerchantPassword());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(LocalDateTime.now());
        // TODO SD: ovo ispraviti kada se naprave stranice na frontu
        request.setSuccessUrl(pspFrontendUrl + "/success");
        request.setFailedUrl(pspFrontendUrl + "/failed");
        request.setErrorUrl(pspFrontendUrl + "/error");
        return request;
    }

}
