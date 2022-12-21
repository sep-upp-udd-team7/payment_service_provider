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
            throw new Exception("Merchant's credentials are incorrect or merchant is not registered");
        }
        RequestDto request = new RequestDto();
        request.setAmount(dto.getAmount());
        request.setMerchantId(dto.getMerchantId());  // TODO: ??????
        request.setMerchantPassword(acquirer.getMerchantPassword());
        request.setMerchantOrderId(dto.getMerchantOrderId());
        request.setMerchantTimestamp(dto.getMerchantTimestamp());
        request.setSuccessUrl(pspFrontendUrl + environment.getProperty("psp.success-payment"));
        request.setFailedUrl(pspFrontendUrl + environment.getProperty("psp.failed-payment"));
        request.setErrorUrl(pspFrontendUrl + environment.getProperty("psp.error-payment"));
        return request;
    }

}
